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

    // ─────────────────────────────────────────
    // AUTH OPERATIONS — only for auth module!
    // ─────────────────────────────────────────

    UserDto createOAuthUser(String email, String firstName, String lastName);

    Optional<UserAuthDto> findAuthByEmail(String email);
    // ↑ returns password hash — ONLY auth module should call this!

    void updatePassword(String email, String newEncodedPassword);
    // ↑ called after password change — no direct repo access from auth!

    // Special DTO that includes password — only for authentication use
    record UserAuthDto(
            Long id,
            String email,
            String password,              // ← password hash included here
            Role role,
            boolean active,
            boolean passwordResetRequired
    ) {}

}