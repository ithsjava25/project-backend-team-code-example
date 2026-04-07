package demo.codeexample.auth;

import demo.codeexample.enums.Role;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Role role;

    public LoginResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }

    /*Why return the role in the response?
    The frontend needs to know what role the user has immediately after login —
    to decide which navigation items to show, which pages to allow etc.
    The token contains it too, but the frontend shouldn't decode JWTs itself for UI logic.*/

}
