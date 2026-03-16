package dev.lucas.time_tracking.exception;

public class DatabaseOperationException extends TimeTrackingException {

    private static final String ERROR_CODE = "DATABASE_ERROR";

    public DatabaseOperationException(String message) {
        super(message, ERROR_CODE);
    }

    public DatabaseOperationException(String operation, String reason) {
        super(String.format("Database operation '%s' failed: %s", operation, reason),
                ERROR_CODE);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
