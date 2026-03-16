package dev.lucas.time_tracking.exception;

public class TokenValidationException extends TimeTrackingException {

    private static final String ERROR_CODE = "INVALID_TOKEN";

    public TokenValidationException(String message) {
        super(message, ERROR_CODE);
    }

    public TokenValidationException(TokenErrorType errorType) {
        super(errorType.getMessage(), ERROR_CODE);
    }

    public TokenValidationException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }

    public enum TokenErrorType {
        EXPIRED("JWT token has expired"),
        MALFORMED("JWT token is malformed"),
        UNSUPPORTED("JWT token is unsupported"),
        INVALID_SIGNATURE("JWT token signature is invalid"),
        EMPTY_CLAIMS("JWT claims string is empty");

        private final String message;

        TokenErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
