package dev.lucas.time_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorResponse = new ErrorResponse();
    }

    @Test
    void testErrorResponseConstructorDefault() {
        assertNotNull(errorResponse);
    }

    @Test
    void testErrorResponseSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        errorResponse.setTimestamp(now);
        errorResponse.setStatus(404);
        errorResponse.setError("Not Found");
        errorResponse.setMessage("Resource not found");
        errorResponse.setErrorCode("RNF001");
        errorResponse.setPath("/api/users");

        assertEquals(now, errorResponse.getTimestamp());
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals("RNF001", errorResponse.getErrorCode());
        assertEquals("/api/users", errorResponse.getPath());
    }

    @Test
    void testErrorResponseBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse built = ErrorResponse.builder()
                .timestamp(now)
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .errorCode("RNF001")
                .path("/api/users")
                .build();

        assertEquals(404, built.getStatus());
        assertEquals("Not Found", built.getError());
    }

    @Test
    void testErrorResponseOfMethodWithoutErrorCode() {
        ErrorResponse response = ErrorResponse.of(
                404,
                "Not Found",
                "Resource not found",
                "/api/users"
        );

        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("Resource not found", response.getMessage());
        assertEquals("/api/users", response.getPath());
        assertNull(response.getErrorCode());
    }

    @Test
    void testErrorResponseOfMethodWithErrorCode() {
        ErrorResponse response = ErrorResponse.of(
                404,
                "Not Found",
                "Resource not found",
                "RNF001",
                "/api/users"
        );

        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("Resource not found", response.getMessage());
        assertEquals("RNF001", response.getErrorCode());
        assertEquals("/api/users", response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testErrorResponseWithValidationErrors() {
        Map<String, String> validationErrors = new HashMap<>();
        validationErrors.put("cpf", "CPF must contain exactly 11 digits");
        validationErrors.put("name", "Name is required");

        errorResponse.setValidationErrors(validationErrors);

        assertNotNull(errorResponse.getValidationErrors());
        assertEquals(2, errorResponse.getValidationErrors().size());
        assertEquals("CPF must contain exactly 11 digits", errorResponse.getValidationErrors().get("cpf"));
    }

    @Test
    void testErrorResponseAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> errors = new HashMap<>();

        ErrorResponse response = new ErrorResponse(
                now,
                400,
                "Bad Request",
                "Invalid input",
                "BR001",
                "/api/register",
                errors
        );

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getError());
        assertEquals("Invalid input", response.getMessage());
    }

    @Test
    void testErrorResponseUnauthorized() {
        ErrorResponse response = ErrorResponse.of(
                401,
                "Unauthorized",
                "Invalid credentials",
                "UNAUTH001",
                "/api/auth"
        );

        assertEquals(401, response.getStatus());
        assertEquals("Unauthorized", response.getError());
    }

    @Test
    void testErrorResponseConflict() {
        ErrorResponse response = ErrorResponse.of(
                409,
                "Conflict",
                "User already exists",
                "CONFLICT001",
                "/api/users"
        );

        assertEquals(409, response.getStatus());
        assertEquals("Conflict", response.getError());
    }

    @Test
    void testErrorResponseInternalServerError() {
        ErrorResponse response = ErrorResponse.of(
                500,
                "Internal Server Error",
                "An unexpected error occurred",
                "ISE001",
                "/api/data"
        );

        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getError());
    }

    @Test
    void testErrorResponseEquality() {
        ErrorResponse response1 = ErrorResponse.of(
                404,
                "Not Found",
                "Resource not found",
                "RNF001",
                "/api/users"
        );

        ErrorResponse response2 = ErrorResponse.of(
                404,
                "Not Found",
                "Resource not found",
                "RNF001",
                "/api/users"
        );

        assertEquals(response1.getStatus(), response2.getStatus());
        assertEquals(response1.getError(), response2.getError());
    }

    @Test
    void testErrorResponseTimestampNotNull() {
        ErrorResponse response = ErrorResponse.of(
                400,
                "Bad Request",
                "Invalid input",
                "/api/test"
        );

        assertNotNull(response.getTimestamp());
    }

    @Test
    void testErrorResponseWithMultipleValidationErrors() {
        Map<String, String> errors = new HashMap<>();
        errors.put("cpf", "Invalid CPF");
        errors.put("password", "Password too short");
        errors.put("email", "Invalid email format");

        errorResponse.setValidationErrors(errors);

        assertEquals(3, errorResponse.getValidationErrors().size());
    }
}
