package demo.codeexample.auth;

// LoginRequest.java — what frontend sends

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // getters & setters MUL LOMBOK TSEKI!
}