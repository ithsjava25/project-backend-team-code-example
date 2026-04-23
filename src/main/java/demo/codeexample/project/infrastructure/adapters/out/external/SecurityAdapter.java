package demo.codeexample.project.infrastructure.adapters.out.external;

import demo.codeexample.project.application.out.SecurityPort;
import demo.codeexample.security.UserAuthHelper;
import demo.codeexample.user.UserLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityAdapter implements SecurityPort {

    private final UserLookup userLookup;

    @Override
    public Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.getPrincipal() instanceof Long id) ? id : null;
    }
    @Override
    public String getCurrentUserName() {
        Long userId = getCurrentUserId();
        if (userId == null) return "System";

        return userLookup.findById(userId)
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown User (" + userId + ")");
    }

}
