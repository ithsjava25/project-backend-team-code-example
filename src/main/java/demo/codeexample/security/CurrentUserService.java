package demo.codeexample.security;

import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserLookup userLookup;

    public Optional<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            return userLookup.findByEmail(customUserDetails.getUsername());
        }

        if (principal instanceof String email && !"anonymousUser".equals(email)) {
            return userLookup.findByEmail(email);
        }

        return Optional.empty();
    }
}