package demo.codeexample.user;

import demo.codeexample.enums.Role;
import demo.codeexample.user.exception.EmailAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // injected by Spring

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public UserEntity createUser(String firstName, String lastName, String email, String tempPassword, Role role) {
//        // Hash before saving - plain text never touches the database
//        String hashedPassword = passwordEncoder.encode(tempPassword);
//
//        UserEntity user = new UserEntity();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setEmail(email);
//        user.setPassword(hashedPassword);  // storing the hash, not "tempPassword"
//        user.setRole(role);
//        user.setPasswordResetRequired(true); // force change on first login
//
//        return userRepository.save(user);
//    }

//    public boolean checkPassword(String rawInput, String storedHash) {
//        // BCrypt compares them correctly - you never decode the hash
//        return passwordEncoder.matches(rawInput, storedHash);
//    }

    public UserResponse createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        String tempPassword = generateTempPassword();

        UserEntity user = new UserEntity();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setRole(request.getRole());
        // isActive & passwordResetRequired default to true already

        UserEntity saved = userRepository.save(user);

        // TODO Step 7: email tempPassword to the new user

        return UserResponse.fromEntity(saved);
    }

    public UserResponse getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return UserResponse.fromEntity(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity) // applies fromEntity to each user
                .toList();
    }

    public UserResponse updateRole(Long id, Role newRole) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setRole(newRole);
        return UserResponse.fromEntity(userRepository.save(user));
    }

    public void deactivateUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

