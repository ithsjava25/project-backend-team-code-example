package demo.codeexample.security;

import demo.codeexample.user.domain.UserRepository;
import org.springframework.stereotype.Component;

@Component("userAuthHelper")
public class UserAuthHelper {

    private final UserRepository userRepository;

    public UserAuthHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Checks if the logged-in user (by email) owns this user ID
    public boolean isOwner(Long userId, String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }
}