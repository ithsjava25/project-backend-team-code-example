package demo.codeexample.project.application.out;

import demo.codeexample.shared.Role;
import demo.codeexample.user.UserLookup;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserPort {

    Optional<UserInfo> findById(Long id);

    List<UserInfo> findByRole(Role role);

    boolean validateEmployees(Set<Long> employeesId);

    List<UserLookup.UserDto> findAll();

    record UserInfo(Long id, String firstname, String lastname, Role role){}
}
