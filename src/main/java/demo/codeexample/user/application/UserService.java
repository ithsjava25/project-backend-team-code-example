package demo.codeexample.user.application;

import demo.codeexample.exceptions.EmailAlreadyExistsException;
import demo.codeexample.shared.Role;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.user.CreateUserDto;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import demo.codeexample.user.infrastructure.EmailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserLookup {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


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
                .map(id -> findById(id).orElseThrow(() -> new UserNotFoundException(id)))
                .map(UserDto::getRole)
                .toList();

        List<Role> requiredRoles = List.of(Role.PRODUCER, Role.DIRECTOR, Role.EDITOR, Role.RECRUITER);
        return new HashSet<>(foundRoles).containsAll(requiredRoles);
    }

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
    public UserDto createUser(CreateUserDto request) {

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
        //user.setActive(true);
        //user.setPasswordResetRequired(true);

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

        //user.setActive(false);
        repository.save(user);
    }


    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
