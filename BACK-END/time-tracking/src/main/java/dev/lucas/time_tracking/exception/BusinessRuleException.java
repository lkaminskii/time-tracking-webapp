package dev.lucas.time_tracking.exception;

public class BusinessRuleException extends TimeTrackingException {

    private static final String ERROR_CODE = "BUSINESS_RULE_VIOLATION";

    public BusinessRuleException(String message) {
        super(message, ERROR_CODE);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}
