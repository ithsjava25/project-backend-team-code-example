package demo.codeexample.project.application.out;

import demo.codeexample.enums.Role;
import demo.codeexample.user.UserLookup;

import java.util.List;
import java.util.Optional;

public interface UserPort {

    Optional<UserInfo> findById(Long id);

    List<UserInfo> findByRole(Role role);

    List<UserLookup.UserDto> findAll();

    record UserInfo(Long id, String firstname, String lastname, Role role){}
}
