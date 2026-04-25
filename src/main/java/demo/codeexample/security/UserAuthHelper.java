package demo.codeexample.security;

import demo.codeexample.user.UserLookup;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Long id) {
            return id;
        }
        return null;
    }
    public String getCurrentUserName() {
        Long userId = getCurrentUserId();
        if (userId == null) return "System";

        return userLookup.findById(userId)
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown User (" + userId + ")");
    }
}