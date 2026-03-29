package demo.codeexample.user;

import demo.codeexample.enums.Role;

public record UserDTO(
        Long id,
        String fullName,
        String email,
        Role role
) {}

//No password here!

