package dev.lucas.time_tracking.exception;

public class InvalidDateFormatException extends TimeTrackingException {

    private static final String ERROR_CODE = "INVALID_DATE_FORMAT";

    public InvalidDateFormatException(String message) {
        super(message, ERROR_CODE);
    }

    public InvalidDateFormatException(String date, String expectedFormat) {
        super(String.format("Invalid date format: '%s'. Expected format: %s", date, expectedFormat),
                ERROR_CODE);
    }

    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
