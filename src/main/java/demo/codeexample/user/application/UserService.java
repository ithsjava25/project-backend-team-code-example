package demo.codeexample.user.application;

import demo.codeexample.user.domain.Role;
import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.user.UserLookup;
import demo.codeexample.exceptions.EmailAlreadyExistsException;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import demo.codeexample.user.infrastructure.EmailService;
import demo.codeexample.user.CreateUserRequestDTO;
import demo.codeexample.user.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements UserLookup {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // injected by Spring
    private final EmailService emailService;

    @Override
    public Optional<UserDto> findByFullName(String firstname, String lastName) {
        return repository.findByFirstNameAndLastNameIgnoreCase(firstname, lastName)
                .map(entity -> mapper.map(entity, UserDto.class));
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, UserDto.class));
    }
    public UserDto createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

    @Override
    public Optional<UserDto> findById(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, UserDto.class));
    }
        String tempPassword = generateTempPassword();

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setRole(request.getRole());
        // isActive & passwordResetRequired default to true

        User saved = userRepository.save(user);

        // REMOVE THIS IN PRODUCTION!
        System.out.println("TEMP PASSWORD FOR " + saved.getEmail() + ": " + tempPassword);

        // Send welcome email with temp password

        /*Why wrap email sending in try-catch?
        Email is an external system — it can fail for many reasons (network issues, wrong config, mail server down).
        You don't want a user creation to fail just because the email didn't send.
        The user is created successfully — maybe retry the email later. This is called graceful degradation.*/
        try {
            emailService.sendWelcomeEmail(
                    saved.getEmail(),
                    saved.getFirstName(),
                    tempPassword  // plain text here — only time we use it!
            );

            /*Why pass tempPassword (plain text) to the email method?
             This is the only moment in the entire system where the plain password exists in memory.
             It's generated, immediately hashed for the database, and also immediately sent by email.
             It's never stored plain anywhere. After this method returns, the plain password is gone forever.*/

        } catch (Exception e) {
            // Don't fail user creation if email fails!
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return UserDto.fromEntity(saved);

    @Override
    public boolean validateUserRole(Long id, Role role) {
        UserDto user = findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return user.role() == role;
    }
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .toList();
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        return UserDto.fromEntity(user);
    }

    @Override
    public List<UserDto> findByRole(Role role) {
        return repository.findAllByRoleIgnoreCase(role).stream()
                .map(entity -> mapper.map(entity, UserDto.class))
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity) // applies fromEntity to each user
                .toList();
    }

    public UserDto updateRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setRole(newRole);
        return UserDto.fromEntity(userRepository.save(user));
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }


}