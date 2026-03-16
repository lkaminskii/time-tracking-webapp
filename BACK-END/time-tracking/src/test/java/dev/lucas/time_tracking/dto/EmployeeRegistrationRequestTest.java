package dev.lucas.time_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRegistrationRequestTest {

    private EmployeeRegistrationRequest request;

    @BeforeEach
    void setUp() {
        request = new EmployeeRegistrationRequest();
    }

    @Test
    void testEmployeeRegistrationRequestConstructorDefault() {
        assertNotNull(request);
    }

    @Test
    void testEmployeeRegistrationRequestSettersAndGetters() {
        request.setCpf("98765432100");
        request.setName("John Doe");
        request.setPis("12345678900");
        request.setPassword("password123");
        request.setCompanyName("Company Name");
        request.setCompanyCnpj("12345678901234");

        assertEquals("98765432100", request.getCpf());
        assertEquals("John Doe", request.getName());
        assertEquals("12345678900", request.getPis());
        assertEquals("password123", request.getPassword());
        assertEquals("Company Name", request.getCompanyName());
        assertEquals("12345678901234", request.getCompanyCnpj());
    }

    @Test
    void testEmployeeRegistrationRequestAllFields() {
        request.setCpf("11111111111");
        request.setName("Jane Smith");
        request.setPis("22222222222");
        request.setPassword("securePass");
        request.setCompanyName("ABC Corporation");
        request.setCompanyCnpj("99999999999999");

        assertNotNull(request.getCpf());
        assertNotNull(request.getName());
        assertNotNull(request.getPis());
        assertNotNull(request.getPassword());
        assertNotNull(request.getCompanyName());
        assertNotNull(request.getCompanyCnpj());
    }

    @Test
    void testEmployeeRegistrationRequestWithValidCpf() {
        String validCpf = "12345678901";
        request.setCpf(validCpf);

        assertEquals(validCpf, request.getCpf());
    }

    @Test
    void testEmployeeRegistrationRequestWithValidPis() {
        String validPis = "11111111111";
        request.setPis(validPis);

        assertEquals(validPis, request.getPis());
    }

    @Test
    void testEmployeeRegistrationRequestWithValidCnpj() {
        String validCnpj = "12345678901234";
        request.setCompanyCnpj(validCnpj);

        assertEquals(validCnpj, request.getCompanyCnpj());
    }

    @Test
    void testEmployeeRegistrationRequestWithNullValues() {
        request.setCpf(null);
        request.setName(null);
        request.setPis(null);
        request.setPassword(null);
        request.setCompanyName(null);
        request.setCompanyCnpj(null);

        assertNull(request.getCpf());
        assertNull(request.getName());
        assertNull(request.getPis());
        assertNull(request.getPassword());
        assertNull(request.getCompanyName());
        assertNull(request.getCompanyCnpj());
    }

    @Test
    void testEmployeeRegistrationRequestEquality() {
        EmployeeRegistrationRequest request1 = new EmployeeRegistrationRequest();
        request1.setCpf("98765432100");
        request1.setName("John Doe");
        request1.setPis("12345678900");

        EmployeeRegistrationRequest request2 = new EmployeeRegistrationRequest();
        request2.setCpf("98765432100");
        request2.setName("John Doe");
        request2.setPis("12345678900");

        assertEquals(request1.getCpf(), request2.getCpf());
        assertEquals(request1.getName(), request2.getName());
    }

    @Test
    void testEmployeeRegistrationRequestWithSpecialCharacters() {
        request.setCompanyName("Company & Co., Ltd.");
        request.setName("João da Silva");

        assertEquals("Company & Co., Ltd.", request.getCompanyName());
        assertEquals("João da Silva", request.getName());
    }

    @Test
    void testEmployeeRegistrationRequestWithEmptyStrings() {
        request.setCpf("");
        request.setName("");
        request.setPassword("");

        assertEquals("", request.getCpf());
        assertEquals("", request.getName());
        assertEquals("", request.getPassword());
    }

    @Test
    void testEmployeeRegistrationRequestUpdateFields() {
        request.setCpf("11111111111");
        request.setName("Initial Name");

        request.setName("Updated Name");

        assertEquals("11111111111", request.getCpf());
        assertEquals("Updated Name", request.getName());
    }

    @Test
    void testEmployeeRegistrationRequestMinimumData() {
        request.setCpf("98765432100");
        request.setPassword("password");

        assertNotNull(request.getCpf());
        assertNotNull(request.getPassword());
    }
}
