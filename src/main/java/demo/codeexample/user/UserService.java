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



//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//    private final PasswordEncoder passwordEncoder;
//
//
//    // 1. FOR THE CONTROLLER (methods to convert Entity to DTO)
//    //Use this when the website wants to see a profile
//    public UserDTO getUserDtoById(Long id) {
//       UserEntity user = findUserById(id);
//        // Calls the helper below
//        return modelMapper.map(user, UserDTO.class);
//    }
//
//
//    // 2. FOR REGISTRATION (Takes Request, returns DTO)
//    public UserDTO registerUser(@Valid CreateUserRequestDTO requestDTO) {
//
//            // 1. Check if email is already taken
//        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already exists!");
//        }
//
//            // 2. Map Request -> Entity
//        UserEntity userEntity = modelMapper.map(requestDTO, UserEntity.class);
//
//            //3. HASH the password before saving
//            // This takes "12345678" and turns it into "$2a$10$..."
//        String hashedPw = passwordEncoder.encode(requestDTO.getPassword());
//        userEntity.setPassword(hashedPw);
//
//            //4. Save DB
//        UserEntity savedUser = userRepository.save(userEntity);
//
//            // 5. Return the safe DTO (which has no password field)
//        return modelMapper.map(savedUser, UserDTO.class);
//    }
//
//        // INTERNAL HELPER (Returns Entity)
//        // Keep this public so it is possible to use it in TaskService!
//        // Example: task.setAssignedUser(userService.findUserById(5L));
//    public UserEntity findUserById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(()->new RuntimeException("User not found with id: " + id));
//    }
}

