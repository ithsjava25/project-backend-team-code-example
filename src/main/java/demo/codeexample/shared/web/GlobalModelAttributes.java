package demo.codeexample.shared.web;

import demo.codeexample.auth.application.CurrentUserService;
import demo.codeexample.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CurrentUserService currentUserService;

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return currentUserService.getCurrentUser().orElse(null);
    }
}