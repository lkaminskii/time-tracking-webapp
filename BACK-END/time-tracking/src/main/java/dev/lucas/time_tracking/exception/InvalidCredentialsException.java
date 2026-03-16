package dev.lucas.time_tracking.exception;

public class InvalidCredentialsException extends TimeTrackingException {

    private static final String ERROR_CODE = "INVALID_CREDENTIALS";

    public InvalidCredentialsException(String message) {
        super(message, ERROR_CODE);
    }

    public InvalidCredentialsException() {
        super("Invalid CPF or password", ERROR_CODE);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
