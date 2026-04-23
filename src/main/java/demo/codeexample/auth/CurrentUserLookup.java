package demo.codeexample.auth;

import demo.codeexample.user.UserDto;

import java.util.Optional;

public interface CurrentUserLookup {
    Optional<UserDto> getCurrentUser();
}