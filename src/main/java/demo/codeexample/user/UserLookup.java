package demo.codeexample.user;

import java.util.List;
import java.util.Optional;

public interface UserLookup {

    // ─────────────────────────────────────────
    // READ OPERATIONS
    // ─────────────────────────────────────────

    List<UserDto> findAll();

    List<UserDto> findByRole(Role role);

    Optional<UserDto> findByFullName(String firstname, String lastName);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findById(Long id);

    boolean validateUserRole(Long id, Role role);

    // ─────────────────────────────────────────
    // WRITE OPERATIONS
    // ─────────────────────────────────────────

    UserDto createUser(CreateUserRequestDTO request);
    UserDto updateRole(Long id, Role newRole);
    void deactivateUser(Long id);

}