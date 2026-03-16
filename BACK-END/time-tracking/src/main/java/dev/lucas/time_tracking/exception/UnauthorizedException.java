package dev.lucas.time_tracking.exception;

public class UnauthorizedException extends TimeTrackingException {

    private static final String ERROR_CODE = "UNAUTHORIZED";

    public UnauthorizedException(String message) {
        super(message, ERROR_CODE);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
