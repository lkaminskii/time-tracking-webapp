package dev.lucas.time_tracking.exception;

public class ResourceNotFoundException extends TimeTrackingException {

    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String message) {
        super(message, ERROR_CODE);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
                ERROR_CODE);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
