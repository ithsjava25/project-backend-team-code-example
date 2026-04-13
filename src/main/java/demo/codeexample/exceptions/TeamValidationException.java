package demo.codeexample.exceptions;

public class TeamValidationException extends RuntimeException {
    public TeamValidationException() {
        super("Team must contain at least one of each role!");
    }
}
