package demo.codeexample.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    // 1. FOR THE CONTROLLER (methods to convert Entity to DTO)
    //Use this when the website wants to see a profile
    public UserDTO getUserDtoById(Long id) {
       UserEntity user = findUserById(id);
        // Calls the helper below
        return modelMapper.map(user, UserDTO.class);
    }


    // 2. FOR REGISTRATION (Takes Request, returns DTO)
    public UserDTO registerUser(@Valid CreateUserRequestDTO requestDTO) {

            // 1. Check if email is already taken
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

            // 2. Map Request -> Entity
        UserEntity userEntity = modelMapper.map(requestDTO, UserEntity.class);

            //3. HASH the password before saving
            // This takes "12345678" and turns it into "$2a$10$..."
        String hashedPw = passwordEncoder.encode(requestDTO.getPassword());
        userEntity.setPassword(hashedPw);

            //4. Save DB
        UserEntity savedUser = userRepository.save(userEntity);

            // 5. Return the safe DTO (which has no password field)
        return modelMapper.map(savedUser, UserDTO.class);
    }

        // INTERNAL HELPER (Returns Entity)
        // Keep this public so it is possible to use it in TaskService!
        // Example: task.setAssignedUser(userService.findUserById(5L));
    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found with id: " + id));
    }
}

