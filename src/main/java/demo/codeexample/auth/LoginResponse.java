package demo.codeexample.auth;

import demo.codeexample.user.Role;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final String token;
    private final Role role;
    private final boolean passwordResetRequired;

    public LoginResponse(String token, Role role, boolean passwordResetRequired) {
        this.token = token;
        this.role = role;
        this.passwordResetRequired = passwordResetRequired;
    }
}
