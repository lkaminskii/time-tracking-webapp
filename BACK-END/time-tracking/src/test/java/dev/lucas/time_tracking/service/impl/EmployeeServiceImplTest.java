package dev.lucas.time_tracking.service.impl;

import dev.lucas.time_tracking.dto.EmployeeRegistrationRequest;
import dev.lucas.time_tracking.dto.JwtAuthenticationResponse;
import dev.lucas.time_tracking.dto.LoginRequest;
import dev.lucas.time_tracking.exception.ResourceNotFoundException;
import dev.lucas.time_tracking.exception.UnauthorizedException;
import dev.lucas.time_tracking.exception.UserAlreadyExistsException;
import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.Role;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import dev.lucas.time_tracking.security.JwtTokenProvider;
import dev.lucas.time_tracking.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private LoginRequest loginRequest;
    private EmployeeRegistrationRequest registrationRequest;
    private String adminCpf;
    private String adminPassword;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        adminCpf = "12345678900";
        adminPassword = "admin123";
        ReflectionTestUtils.setField(employeeService, "adminCpf", adminCpf);
        ReflectionTestUtils.setField(employeeService, "adminPassword", adminPassword);

        employee = Employee.builder()
                .id(1L)
                .cpf("98765432100")
                .name("John Doe")
                .pis("12345678900")
                .password("encodedPassword")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setCpf("98765432100");
        loginRequest.setPassword("password123");
        loginRequest.setRememberMe(false);

        registrationRequest = new EmployeeRegistrationRequest();
        registrationRequest.setCpf("11111111111");
        registrationRequest.setName("Jane Doe");
        registrationRequest.setPis("22222222222");
        registrationRequest.setPassword("password123");
        registrationRequest.setCompanyName("New Company");
        registrationRequest.setCompanyCnpj("12345678901234");

        authentication = new UsernamePasswordAuthenticationToken("98765432100", "password123");
    }

    @Test
    void testAuthenticateEmployeeSuccess() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(tokenProvider.generateToken(authentication, false)).thenReturn("jwt_token");

        JwtAuthenticationResponse response = employeeService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("98765432100", response.getCpf());
        assertEquals("John Doe", response.getName());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenProvider, times(1)).generateToken(authentication, false);
    }

    @Test
    void testAuthenticateAdminDelegates() {
        loginRequest.setCpf(adminCpf);
        when(authenticationService.authenticate(loginRequest)).thenReturn(
                new JwtAuthenticationResponse("admin_token", "Bearer", adminCpf, "Administrator", "ADMIN"));

        JwtAuthenticationResponse response = employeeService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals(adminCpf, response.getCpf());
        verify(authenticationService, times(1)).authenticate(loginRequest);
    }

    @Test
    void testAuthenticateEmployeeNotFound() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.empty());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> employeeService.authenticate(loginRequest));

        assertEquals("Invalid CPF or password", exception.getMessage());
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(
                new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> employeeService.authenticate(loginRequest));

        assertEquals("Invalid CPF or password", exception.getMessage());
    }

    @Test
    void testRegisterEmployeeSuccess() {
        doNothing().when(authenticationService).validateAdminCredentials(adminCpf, adminPassword);
        when(employeeRepository.existsByCpf(registrationRequest.getCpf())).thenReturn(false);
        when(employeeRepository.existsByPis(registrationRequest.getPis())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encoded_password");
        when(employeeRepository.save(any())).thenReturn(
                Employee.builder()
                        .id(2L)
                        .cpf(registrationRequest.getCpf())
                        .name(registrationRequest.getName())
                        .pis(registrationRequest.getPis())
                        .password("encoded_password")
                        .companyName(registrationRequest.getCompanyName())
                        .companyCnpj(registrationRequest.getCompanyCnpj())
                        .role(Role.EMPLOYEE)
                        .build()
        );

        EmployeeRegistrationRequest result = employeeService.registerEmployee(registrationRequest, adminCpf);

        assertNotNull(result);
        assertEquals(registrationRequest.getCpf(), result.getCpf());
        assertEquals(registrationRequest.getName(), result.getName());
        verify(employeeRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(registrationRequest.getPassword());
    }

    @Test
    void testRegisterEmployeeCpfAlreadyExists() {
        doNothing().when(authenticationService).validateAdminCredentials(adminCpf, adminPassword);
        when(employeeRepository.existsByCpf(registrationRequest.getCpf())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> employeeService.registerEmployee(registrationRequest, adminCpf));

        assertTrue(exception.getMessage().contains("CPF"));
    }

    @Test
    void testRegisterEmployeePisAlreadyExists() {
        doNothing().when(authenticationService).validateAdminCredentials(adminCpf, adminPassword);
        when(employeeRepository.existsByCpf(registrationRequest.getCpf())).thenReturn(false);
        when(employeeRepository.existsByPis(registrationRequest.getPis())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> employeeService.registerEmployee(registrationRequest, adminCpf));

        assertTrue(exception.getMessage().contains("PIS"));
    }

    @Test
    void testRegisterEmployeeInvalidAdminCredentials() {
        doThrow(new UnauthorizedException("Invalid admin credentials"))
                .when(authenticationService).validateAdminCredentials(adminCpf, adminPassword);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> employeeService.registerEmployee(registrationRequest, adminCpf));

        assertEquals("Invalid admin credentials", exception.getMessage());
    }

    @Test
    void testGetEmployeeByCpfSuccess() {
        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));

        EmployeeRegistrationRequest result = employeeService.getEmployeeByCpf("98765432100");

        assertNotNull(result);
        assertEquals("98765432100", result.getCpf());
        assertEquals("John Doe", result.getName());
        assertEquals("12345678900", result.getPis());
        verify(employeeRepository, times(1)).findByCpf("98765432100");
    }

    @Test
    void testGetEmployeeByCpfNotFound() {
        when(employeeRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeByCpf("99999999999"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testRegisterEmployeeNullAdminCpf() {
        ReflectionTestUtils.setField(employeeService, "adminCpf", null);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(tokenProvider.generateToken(authentication, false)).thenReturn("jwt_token");

        JwtAuthenticationResponse response = employeeService.authenticate(loginRequest);

        assertNotNull(response);
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
