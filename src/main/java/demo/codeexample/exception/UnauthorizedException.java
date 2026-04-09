package demo.codeexample.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }

    /*Why RuntimeException and not Exception?
    Checked exceptions (Exception) force every caller to handle them
    with try-catch or declare throws. That creates a lot of boilerplate.
    RuntimeException is unchecked — it bubbles up naturally to your
    global handler without cluttering every method signature.*/

}
