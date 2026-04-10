package demo.codeexample.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // getters

    /*Why a dedicated class instead of a Map?
    A class gives you a guaranteed, consistent structure.
    A Map can have any keys — different methods might return different keys,
    making the frontend's job harder.*/
}
