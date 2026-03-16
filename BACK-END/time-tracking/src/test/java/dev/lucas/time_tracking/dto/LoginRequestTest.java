package dev.lucas.time_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    void testLoginRequestConstructorDefault() {
        assertNotNull(loginRequest);
        assertFalse(loginRequest.isRememberMe());
    }

    @Test
    void testLoginRequestSettersAndGetters() {
        loginRequest.setCpf("98765432100");
        loginRequest.setPassword("password123");
        loginRequest.setRememberMe(true);

        assertEquals("98765432100", loginRequest.getCpf());
        assertEquals("password123", loginRequest.getPassword());
        assertTrue(loginRequest.isRememberMe());
    }

    @Test
    void testLoginRequestCpfValidation() {
        loginRequest.setCpf("12345678900");
        assertEquals("12345678900", loginRequest.getCpf());
    }

    @Test
    void testLoginRequestPasswordValidation() {
        loginRequest.setPassword("securePassword123");
        assertEquals("securePassword123", loginRequest.getPassword());
    }

    @Test
    void testLoginRequestRememberMeDefaultFalse() {
        assertFalse(loginRequest.isRememberMe());
    }

    @Test
    void testLoginRequestRememberMeTrue() {
        loginRequest.setRememberMe(true);
        assertTrue(loginRequest.isRememberMe());
    }

    @Test
    void testLoginRequestAllFieldsSet() {
        loginRequest.setCpf("11111111111");
        loginRequest.setPassword("myPassword");
        loginRequest.setRememberMe(false);

        assertNotNull(loginRequest.getCpf());
        assertNotNull(loginRequest.getPassword());
        assertFalse(loginRequest.isRememberMe());
    }

    @Test
    void testLoginRequestWithNullCpf() {
        loginRequest.setCpf(null);
        assertNull(loginRequest.getCpf());
    }

    @Test
    void testLoginRequestWithNullPassword() {
        loginRequest.setPassword(null);
        assertNull(loginRequest.getPassword());
    }

    @Test
    void testLoginRequestEquality() {
        LoginRequest request1 = new LoginRequest();
        request1.setCpf("98765432100");
        request1.setPassword("password123");
        request1.setRememberMe(true);

        LoginRequest request2 = new LoginRequest();
        request2.setCpf("98765432100");
        request2.setPassword("password123");
        request2.setRememberMe(true);

        assertEquals(request1.getCpf(), request2.getCpf());
        assertEquals(request1.getPassword(), request2.getPassword());
        assertEquals(request1.isRememberMe(), request2.isRememberMe());
    }

    @Test
    void testLoginRequestWithEmptyStrings() {
        loginRequest.setCpf("");
        loginRequest.setPassword("");

        assertEquals("", loginRequest.getCpf());
        assertEquals("", loginRequest.getPassword());
    }

    @Test
    void testLoginRequestToggleRememberMe() {
        assertFalse(loginRequest.isRememberMe());
        loginRequest.setRememberMe(true);
        assertTrue(loginRequest.isRememberMe());
        loginRequest.setRememberMe(false);
        assertFalse(loginRequest.isRememberMe());
    }
}
