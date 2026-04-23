package demo.codeexample.auth.application;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof Long userId) {
            return userId;
        }

        throw new IllegalStateException("Unsupported principal type: " + principal);
    }
}