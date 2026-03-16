package dev.lucas.time_tracking.exception;

public abstract class TimeTrackingException extends RuntimeException {

    private final String errorCode;

    protected TimeTrackingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected TimeTrackingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
