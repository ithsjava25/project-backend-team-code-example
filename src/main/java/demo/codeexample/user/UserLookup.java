package demo.codeexample.user;

import demo.codeexample.shared.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserLookup {

    List<UserDto> findAll();

    List<UserDto> findByRole(Role role);

    Optional<UserDto> findByFullName(String firstname, String lastName);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findById(Long id);

    boolean validateUniqueRoles(Set<Long> employeesId);

    UserDto createUser(CreateUserRequestDTO request);
    UserDto updateRole(Long id, Role newRole);
    void deactivateUser(Long id);

//    record UserDto(Long id, String firstName, String lastName, String email, Role role){}
}

