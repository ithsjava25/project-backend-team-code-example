package demo.codeexample.user.application;

import demo.codeexample.user.doman.Role;
import demo.codeexample.exception.UserNotFoundException;
import demo.codeexample.exception.EmailAlreadyExistsException;
import demo.codeexample.user.doman.User;
import demo.codeexample.user.doman.UserRepository;
import demo.codeexample.user.infrastructure.EmailService;
import demo.codeexample.user.web.CreateUserRequestDTO;
import demo.codeexample.user.web.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // injected by Spring
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public UserResponse createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
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

        return UserResponse.fromEntity(saved);

    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        return UserResponse.fromEntity(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity) // applies fromEntity to each user
                .toList();
    }

    public UserResponse updateRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setRole(newRole);
        return UserResponse.fromEntity(userRepository.save(user));
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