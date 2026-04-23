package demo.codeexample.shared.web;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CurrentUserLookup currentUserLookup;

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return currentUserLookup.getCurrentUser().orElse(null);
    }
}