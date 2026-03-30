package demo.codeexample.user;

import demo.codeexample.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    Long id;
    String fullName;
    String email;
    Role role;
}

//No password here!

