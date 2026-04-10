package demo.codeexample.exceptions;

public class UserNotFoundException extends RuntimeException {



    public UserNotFoundException(Long id) {
        super("User with id " + id + " does not exist in database");
    }

    // Keep for flexibility
    public UserNotFoundException(String message) {
        super(message);
    }
}
