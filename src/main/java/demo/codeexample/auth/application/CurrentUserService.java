package demo.codeexample.auth.application;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.auth.infrastructure.CustomUserDetails;
import demo.codeexample.user.UserDto;
import demo.codeexample.user.UserLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentUserService implements CurrentUserLookup {

    private final UserLookup userLookup;

    @Override
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

        if (principal instanceof Long userId) {
            return userLookup.findById(userId);
        }

        return Optional.empty();
    }
}