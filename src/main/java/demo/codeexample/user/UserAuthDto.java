package demo.codeexample.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {

    private Long id;
    private String email;
    private String password;          // ← password hash — auth use only!
    private Role role;
    private boolean active;
    private boolean passwordResetRequired;
}