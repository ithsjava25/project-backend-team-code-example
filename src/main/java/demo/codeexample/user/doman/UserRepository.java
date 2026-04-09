package demo.codeexample.user.doman;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* Why @Repository? It tells Spring this is a data-access component.
 * It also enables Spring to translate database exceptions into
 * Spring's own exception hierarchy — cleaner error handling. */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);
    // finds all users with a specific role
    // useful for admin: "show me all RECRUITERs"
}



