package demo.codeexample.user;

import demo.codeexample.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserLookup {

    List<UserDto> findAll();

    List<UserDto> findByRole(Role role);

    Optional<UserDto> findByFullName(String firstname, String lastName);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findById(Long id);

    record UserDto(Long id, String firstName, String lastName, String email, Role role){}
}

