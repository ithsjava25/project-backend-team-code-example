package demo.codeexample.user;

import demo.codeexample.shared.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean active;
    private boolean passwordResetRequired;

    public String getInitial() {
        if (firstName != null && !firstName.isBlank()) {
            return firstName.substring(0, 1).toUpperCase();
        }
        return "?";
    }

    public String getFullName() {
        String first = firstName != null ? firstName : "";
        String last = lastName != null ? lastName : "";
        return (first + " " + last).trim();
    }

    public String getRoleName() {
        return role != null ? role.name() : "";
    }
}
