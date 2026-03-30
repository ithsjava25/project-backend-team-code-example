package demo.codeexample.user;

import demo.codeexample.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    Long id;
    String fullName;
    String email;
    Role role;
}

//No password here!

