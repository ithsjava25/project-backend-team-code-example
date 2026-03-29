package demo.codeexample.user;

import demo.codeexample.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);



    List<UserEntity> findAllByRoleIgnoreCase (Role role);

}
