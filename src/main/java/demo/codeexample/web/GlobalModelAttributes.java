package demo.codeexample.web;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.user.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAttributes {

    private final CurrentUserLookup currentUserLookup;

    public GlobalModelAttributes(CurrentUserLookup currentUserLookup) {
        this.currentUserLookup = currentUserLookup;
    }

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return currentUserLookup.getCurrentUser().orElse(null);
    }
}