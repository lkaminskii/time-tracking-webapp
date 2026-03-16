package dev.lucas.time_tracking.exception;

public class AccessDeniedException extends TimeTrackingException {

    private static final String ERROR_CODE = "ACCESS_DENIED";

    public AccessDeniedException(String message) {
        super(message, ERROR_CODE);
    }

    public AccessDeniedException() {
        super("You don't have permission to access this resource", ERROR_CODE);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
