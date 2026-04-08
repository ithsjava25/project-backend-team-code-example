package demo.codeexample.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
public class ChangePasswordRequest {

    /*Why ask for currentPassword when changing? This is called re-authentication.
    Even if someone steals an active session/token,
    they can't change the password without knowing the current one.
    This prevents account takeover.*/

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    //getter setter
}
