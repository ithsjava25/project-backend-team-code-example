package demo.codeexample.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }

    // Keep your string version too for flexibility
    public UserNotFoundException(String message) {
        super(message);
    }
}