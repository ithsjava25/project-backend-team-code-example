package demo.codeexample.user.application;

import demo.codeexample.exceptions.EmailAlreadyExistsException;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.shared.Role;
import demo.codeexample.user.*;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import demo.codeexample.user.infrastructure.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserLookup, UserAuthPort {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .toList();
    }

    @Override
    public List<UserDto> findByRole(Role role) {
        return repository.findAllByRoleIgnoreCase(role).stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .toList();
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    @Override
    public Optional<UserDto> findByFullName(String firstname, String lastName) {
        return repository.findByFirstNameAndLastNameIgnoreCase(firstname, lastName)
                .map(entity -> mapper.map(entity, UserDto.class));
    }

    /**
     * Checks that each unique role is present. Starts by converting id -> UserDto in order to see if user exists.
     * Continues with mapping each user to their role.
     * Is possible to have more than one employee with each role.
     * @param employeesId
     * @return true if each user has unique role
     */


    @Override
    public boolean validateUniqueRoles(Set<Long> employeesId) {
        List<Role> foundRoles = employeesId.stream()
                .map(id -> findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id)))
                .map(UserDto::getRole)
                .toList();

        List<Role> requiredRoles = List.of(
                Role.PRODUCER, Role.DIRECTOR, Role.EDITOR, Role.RECRUITER
        );
        return foundRoles.containsAll(requiredRoles);
    }

    @Override
    public boolean validateUserRole(Long id, Role role) {
        UserDto user = findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user.getRole() == role;
    }

    // ─────────────────────────────────────────
    // WRITE OPERATIONS
    // ─────────────────────────────────────────

    @Override
    public UserDto createUser(CreateUserDto request) {

        // Check email is not already taken
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: "
                    + request.getEmail());
        }

        String tempPassword = generateTempPassword();

        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getRole(),
                passwordEncoder.encode(tempPassword)
        );
        user.setActive(true);
        user.setPasswordResetRequired(true);

        User saved = repository.save(user);

        // Send welcome email — don't fail if email fails
        try {
            emailService.sendWelcomeEmail(
                    saved.getEmail(),
                    saved.getFirstName(),
                    tempPassword  // plain text — only moment it exists!
            );
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return mapper.map(saved, UserDto.class);
    }

    @Override
    public UserDto updateRole(Long id, Role newRole) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setRole(newRole);
        return mapper.map(repository.save(user), UserDto.class);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(false);
        repository.save(user);
    }

    @Override
    @Transactional
    public UserDto resetPassword(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Generate new temp password
        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setPasswordResetRequired(true);  // ← force change on next login!

        User saved = repository.save(user);

        // Send email with new temp password
        try {
            emailService.sendPasswordResetEmail(
                    saved.getEmail(),
                    saved.getFirstName(),
                    tempPassword
            );
        } catch (Exception e) {
            log.error("Failed to send reset email for user id {}", id, e);
            throw new IllegalStateException("Failed to send reset email", e);
        }

        return mapper.map(saved, UserDto.class);
    }

    // ─────────────────────────────────────────
    // AUTH OPERATIONS
    // ─────────────────────────────────────────

    @Override
    public Optional<UserAuthDto> findAuthByEmail(String email) {
        return repository.findByEmail(email)
                .map(user -> new UserAuthDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole(),
                        user.isActive(),
                        user.isPasswordResetRequired()
                ));
    }

    @Override
    public void updatePassword(String email, String newEncodedPassword) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        user.setPassword(newEncodedPassword);
        user.setPasswordResetRequired(false);
        repository.save(user);
    }

    @Override
    public UserDto createOAuthUser(String email,
                                   String firstName,
                                   String lastName) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName != null ? firstName : "Unknown");
        newUser.setLastName(lastName   != null ? lastName  : "Unknown");
        newUser.setPassword("OAUTH2_USER_NO_PASSWORD");
        newUser.setRole(Role.VISITOR);
        newUser.setActive(true);
        newUser.setPasswordResetRequired(false);
        return mapper.map(repository.save(newUser), UserDto.class);
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String TEMP_PASSWORD_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    private String generateTempPassword() {
        int length = 14;

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(TEMP_PASSWORD_CHARS.length());
            password.append(TEMP_PASSWORD_CHARS.charAt(index));
        }

        return password.toString();
    }
}