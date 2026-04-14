package demo.codeexample.security;

import demo.codeexample.user.UserLookup;
import org.springframework.stereotype.Component;

@Component("userAuthHelper")
public class UserAuthHelper {

    private final UserLookup userLookup;

    public UserAuthHelper(UserLookup userLookup) {
        this.userLookup = userLookup;
    }

    // Checks if the logged-in user (by email) owns this user ID
    public boolean isOwner(Long userId, String email) {
        return userLookup.findByEmail(email)
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }
}