package demo.codeexample.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
public class ChangePasswordRequest {

    // public DTO

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

}
