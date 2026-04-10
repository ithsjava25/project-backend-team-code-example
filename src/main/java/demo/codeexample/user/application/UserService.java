package demo.codeexample.user.application;

import demo.codeexample.exceptions.EmailAlreadyExistsException;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.user.CreateUserRequestDTO;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import demo.codeexample.user.Role;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import demo.codeexample.user.infrastructure.EmailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserLookup {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ─────────────────────────────────────────
    // READ OPERATIONS
    // ─────────────────────────────────────────

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
    public UserDto createUser(CreateUserRequestDTO request) {

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

    // ─────────────────────────────────────────
    // AUTH OPERATIONS
    // ─────────────────────────────────────────

    @Override
    public UserDto createOAuthUser(String email, String firstName, String lastName) {

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName != null ? firstName : "Unknown");
        newUser.setLastName(lastName != null ? lastName : "Unknown");
        newUser.setPassword("OAUTH2_USER_NO_PASSWORD");
        newUser.setRole(Role.VISITOR);
        newUser.setActive(true);
        newUser.setPasswordResetRequired(false);

        return mapper.map(repository.save(newUser), UserDto.class);
    }


    @Override
    public Optional<UserAuthDto> findAuthByEmail(String email) {
        return repository.findByEmail(email)
                .map(user -> new UserAuthDto(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),        // ← only time password leaves user module
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
        user.setPasswordResetRequired(false);  // ← reset flag cleared automatically
        repository.save(user);
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}