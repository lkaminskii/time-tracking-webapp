package dev.lucas.time_tracking.exception;

public class UserAlreadyExistsException extends TimeTrackingException {

    private static final String ERROR_CODE = "USER_ALREADY_EXISTS";

    public UserAlreadyExistsException(String message) {
        super(message, ERROR_CODE);
    }

    public UserAlreadyExistsException(String fieldName, String fieldValue) {
        super(String.format("User with %s '%s' already exists", fieldName, fieldValue),
                ERROR_CODE);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}