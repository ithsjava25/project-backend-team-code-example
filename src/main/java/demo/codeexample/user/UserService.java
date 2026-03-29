package demo.codeexample.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Method to convert Entity to DTO
    private UserDTO convertToDTO(UserEntity entity) {
        return new UserDTO(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getRole()
        );
    }

    // Logic to save a user
    public UserDTO registerUser(UserEntity user) {
        // 1. Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 2. Save to DB
        UserEntity saved = userRepository.save(user);
        // 3. Return the "safe" DTO
        return convertToDTO(saved);
    }

}
