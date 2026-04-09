package demo.codeexample.exceptions;

public class UserAuthorizationException extends RuntimeException {
    public UserAuthorizationException(Long id) {
        super("User with id " + id + " does not have required authorization.");
    }
}
