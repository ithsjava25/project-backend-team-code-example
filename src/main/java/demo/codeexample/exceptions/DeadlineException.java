package demo.codeexample.exceptions;

public class DeadlineException extends RuntimeException {
    public DeadlineException() {
        super("Invalid deadline for one or more tasks");
    }
}
