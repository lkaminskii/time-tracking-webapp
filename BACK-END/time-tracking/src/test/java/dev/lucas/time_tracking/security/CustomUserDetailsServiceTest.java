package dev.lucas.time_tracking.security;

import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.Role;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private Employee employee;
    private String cpf;

    @BeforeEach
    void setUp() {
        cpf = "98765432100";
        employee = Employee.builder()
                .id(1L)
                .cpf(cpf)
                .name("John Doe")
                .pis("12345678900")
                .password("encoded_password")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));

        UserDetails userDetails = userDetailsService.loadUserByUsername(cpf);

        assertNotNull(userDetails);
        assertEquals(cpf, userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        verify(employeeRepository, times(1)).findByCpf(cpf);
    }

    @Test
    void testLoadUserByUsernameReturnsUserPrincipal() {
        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));

        UserDetails userDetails = userDetailsService.loadUserByUsername(cpf);

        assertInstanceOf(UserPrincipal.class, userDetails);
    }

    @Test
    void testLoadUserByUsernameThrowsExceptionWhenNotFound() {
        when(employeeRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("99999999999"));

        assertTrue(exception.getMessage().contains("Employee not found"));
    }

    @Test
    void testLoadUserByUsernameWithAdminRole() {
        Employee admin = Employee.builder()
                .id(2L)
                .cpf("12345678900")
                .name("Administrator")
                .pis("11111111111")
                .password("admin_password")
                .companyName("System")
                .companyCnpj("00000000000000")
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        when(employeeRepository.findByCpf("12345678900")).thenReturn(Optional.of(admin));

        UserDetails userDetails = userDetailsService.loadUserByUsername("12345678900");

        assertNotNull(userDetails);
        assertEquals("12345678900", userDetails.getUsername());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsernameMultipleCalls() {
        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));

        UserDetails userDetails1 = userDetailsService.loadUserByUsername(cpf);
        UserDetails userDetails2 = userDetailsService.loadUserByUsername(cpf);

        assertNotNull(userDetails1);
        assertNotNull(userDetails2);
        assertEquals(userDetails1.getUsername(), userDetails2.getUsername());
        verify(employeeRepository, times(2)).findByCpf(cpf);
    }

    @Test
    void testLoadUserByUsernameReturnsCorrectAuthorities() {
        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));

        UserDetails userDetails = userDetailsService.loadUserByUsername(cpf);

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE")));
    }

    @Test
    void testLoadUserByUsernameReturnsEnabledUser() {
        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));

        UserDetails userDetails = userDetailsService.loadUserByUsername(cpf);

        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testLoadUserByUsernameWithDifferentCpfs() {
        String cpf2 = "11111111111";
        Employee employee2 = Employee.builder()
                .id(3L)
                .cpf(cpf2)
                .name("Jane Doe")
                .pis("22222222222")
                .password("password2")
                .companyName("Company2")
                .companyCnpj("22222222222222")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByCpf(cpf2)).thenReturn(Optional.of(employee2));

        UserDetails userDetails1 = userDetailsService.loadUserByUsername(cpf);
        UserDetails userDetails2 = userDetailsService.loadUserByUsername(cpf2);

        assertEquals(cpf, userDetails1.getUsername());
        assertEquals(cpf2, userDetails2.getUsername());
    }

    @Test
    void testLoadUserByUsernameNullCpf() {
        when(employeeRepository.findByCpf(null)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null));

        assertNotNull(exception);
    }

    @Test
    void testLoadUserByUsernameEmptyCpf() {
        when(employeeRepository.findByCpf("")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(""));

        assertNotNull(exception);
    }
}
