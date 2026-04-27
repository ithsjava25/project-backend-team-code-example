package demo.codeexample.web;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = {
        "demo.codeexample.project.infrastructure.adapters.in",
        "demo.codeexample.web.web",
        "demo.codeexample.s3FileStorage.web",
        "demo.codeexample.company.web",
        "demo.codeexample.comment.web",
        "demo.codeexample.task.infrastructure.adapters.in"
})
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CurrentUserLookup currentUserLookup;

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return currentUserLookup.getCurrentUser().orElse(null);
    }
}