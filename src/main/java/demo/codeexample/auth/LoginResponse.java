package demo.codeexample.auth;

import demo.codeexample.user.domain.Role;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private Role role;
    private boolean passwordResetRequired;

    public LoginResponse(String token, Role role, boolean passwordResetRequired) {
        this.token = token;
        this.role = role;
        this.passwordResetRequired = passwordResetRequired;
    }


    /*Why return the role in the response?
    The frontend needs to know what role the user has immediately after login —
    to decide which navigation items to show, which pages to allow etc.
    The token contains it too, but the frontend shouldn't decode JWTs itself for UI logic.*/

    /*Why tell the frontend about passwordResetRequired?
    The frontend needs to immediately redirect the user to
    a "change your password" page after login if this is true.
    Without this signal, the frontend doesn't know —
    the user would just land on the dashboard with restricted access,
    which is confusing.*/
}
