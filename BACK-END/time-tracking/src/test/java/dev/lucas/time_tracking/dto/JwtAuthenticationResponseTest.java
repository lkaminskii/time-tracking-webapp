package dev.lucas.time_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationResponseTest {

    private JwtAuthenticationResponse response;

    @BeforeEach
    void setUp() {
        response = new JwtAuthenticationResponse();
    }

    @Test
    void testJwtAuthenticationResponseConstructorDefault() {
        assertNotNull(response);
        assertEquals("Bearer", response.getType());
    }

    @Test
    void testJwtAuthenticationResponseSettersAndGetters() {
        response.setToken("test_token");
        response.setType("Bearer");
        response.setCpf("98765432100");
        response.setName("John Doe");
        response.setRole("EMPLOYEE");

        assertEquals("test_token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("98765432100", response.getCpf());
        assertEquals("John Doe", response.getName());
        assertEquals("EMPLOYEE", response.getRole());
    }

    @Test
    void testJwtAuthenticationResponseBuilder() {
        JwtAuthenticationResponse builtResponse = JwtAuthenticationResponse.builder()
                .token("jwt_token")
                .cpf("98765432100")
                .name("Jane Doe")
                .role("ADMIN")
                .build();

        assertEquals("jwt_token", builtResponse.getToken());
        assertEquals("98765432100", builtResponse.getCpf());
        assertEquals("Jane Doe", builtResponse.getName());
        assertEquals("ADMIN", builtResponse.getRole());
    }

    @Test
    void testJwtAuthenticationResponseBuilderWithType() {
        JwtAuthenticationResponse builtResponse = JwtAuthenticationResponse.builder()
                .token("jwt_token")
                .type("Bearer")
                .cpf("11111111111")
                .name("Admin User")
                .role("ADMIN")
                .build();

        assertEquals("Bearer", builtResponse.getType());
    }

    @Test
    void testJwtAuthenticationResponseAllArgsConstructor() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                "token123",
                "Bearer",
                "12345678900",
                "John Smith",
                "EMPLOYEE"
        );

        assertEquals("token123", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("12345678900", response.getCpf());
        assertEquals("John Smith", response.getName());
        assertEquals("EMPLOYEE", response.getRole());
    }

    @Test
    void testJwtAuthenticationResponseEquality() {
        JwtAuthenticationResponse response1 = JwtAuthenticationResponse.builder()
                .token("token")
                .cpf("98765432100")
                .name("John")
                .role("EMPLOYEE")
                .build();

        JwtAuthenticationResponse response2 = JwtAuthenticationResponse.builder()
                .token("token")
                .cpf("98765432100")
                .name("John")
                .role("EMPLOYEE")
                .build();

        assertEquals(response1, response2);
    }

    @Test
    void testJwtAuthenticationResponseNotNull() {
        JwtAuthenticationResponse builtResponse = JwtAuthenticationResponse.builder()
                .token("jwt_token")
                .cpf("98765432100")
                .name("Jane Doe")
                .role("ADMIN")
                .build();

        assertNotNull(builtResponse);
        assertNotNull(builtResponse.getToken());
        assertNotNull(builtResponse.getCpf());
    }

    @Test
    void testJwtAuthenticationResponseWithNullValues() {
        response.setToken(null);
        response.setCpf(null);
        response.setName(null);

        assertNull(response.getToken());
        assertNull(response.getCpf());
        assertNull(response.getName());
    }

    @Test
    void testJwtAuthenticationResponseDefaultType() {
        JwtAuthenticationResponse newResponse = new JwtAuthenticationResponse();
        assertEquals("Bearer", newResponse.getType());
    }
}
