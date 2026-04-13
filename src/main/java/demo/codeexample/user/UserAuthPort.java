package demo.codeexample.user;

import java.util.Optional;

/**
 * Auth-facing port — ONLY to be used by auth and security modules.
 * Contains sensitive operations involving password hashes.
 */
public interface UserAuthPort {

    Optional<UserAuthDto> findAuthByEmail(String email);

    void updatePassword(String email, String newEncodedPassword);

    UserDto createOAuthUser(String email, String firstName, String lastName);

}
