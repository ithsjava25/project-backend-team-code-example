package demo.codeexample.auth.application;

import demo.codeexample.auth.infrastructure.CustomUserDetails;
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

        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getId();
        }

        String principalClassName = (principal != null)
                ? principal.getClass().getName()
                : "null";

        throw new IllegalStateException(
                "Unsupported principal type: " + principalClassName
        );
    }
}