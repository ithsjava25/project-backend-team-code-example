package demo.codeexample.user;

import java.util.List;
import java.util.Optional;

public interface UserLookup {

    List<UserDto> findAll();

    List<UserDto> findByRole(Role role);

    Optional<UserDto> findByFullName(String firstname, String lastName);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findById(Long id);

    boolean validateUserRole(Long id, Role role);

    UserDto createUser(CreateUserRequestDTO request);
    UserDto updateRole(Long id, Role newRole);
    void deactivateUser(Long id);

//    record UserDto(Long id, String firstName, String lastName, String email, Role role){}
}

