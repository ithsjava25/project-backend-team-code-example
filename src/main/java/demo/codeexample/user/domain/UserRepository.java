package demo.codeexample.user.domain;

import demo.codeexample.shared.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByRoleIgnoreCase (Role role);

    List<User> findByRole(Role role);

    Optional<User> findByFirstNameAndLastNameIgnoreCase(String firstname, String lastName);
}
