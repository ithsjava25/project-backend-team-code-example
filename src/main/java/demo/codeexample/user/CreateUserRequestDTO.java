package demo.codeexample.user;

import demo.codeexample.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/* This is what the frontend sends when creating a user.
    Notice — no password field! Admin creates the user,
    system generates a temp password.*/

@Data
public class CreateUserRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    /* Why validation annotations like @NotBlank and @Email?
    So you catch bad input before it reaches your service or database.
    Your controller will use @Valid to trigger these — we'll see that shortly.
    Always validate at the boundary where data enters your system. */


//    @NotBlank(message = "FUll name is required")
//    private String fullName;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Email should be valid")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 8, message = "Password must be at least 8 characters")
//    private String password;
//
//    @NotNull(message = "Role is required")
//    private Role role;

}
