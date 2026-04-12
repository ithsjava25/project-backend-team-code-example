package demo.codeexample.project.application.out;

import demo.codeexample.user.Role;
import demo.codeexample.user.UserLookup;

import java.util.List;
import java.util.Optional;

public interface UserPort {

    Optional<UserInfo> findById(Long id);

    List<UserInfo> findByRole(Role role);

    boolean validateProducer(Long id);

    List<UserLookup.UserDto> findAll();

    record UserInfo(Long id, String firstname, String lastname, Role role){}
}
