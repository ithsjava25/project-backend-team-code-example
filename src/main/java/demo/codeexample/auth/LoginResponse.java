package demo.codeexample.auth;

import demo.codeexample.shared.Role;
import lombok.Getter;

@Getter
public class LoginResponse {

    // public DTO

    private final String token;
    private final Role role;
    private final boolean passwordResetRequired;

    public LoginResponse(String token, Role role, boolean passwordResetRequired) {
        this.token = token;
        this.role = role;
        this.passwordResetRequired = passwordResetRequired;
    }
}
