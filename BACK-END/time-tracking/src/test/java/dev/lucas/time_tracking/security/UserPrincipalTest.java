package dev.lucas.time_tracking.security;

import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserPrincipalTest {

    private Employee employee;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .cpf("98765432100")
                .name("John Doe")
                .pis("12345678900")
                .password("encoded_password")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        userPrincipal = new UserPrincipal(employee);
    }

    @Test
    void testUserPrincipalConstructor() {
        assertNotNull(userPrincipal);
        assertEquals(1L, userPrincipal.getId());
        assertEquals("98765432100", userPrincipal.getCpf());
        assertEquals("John Doe", userPrincipal.getName());
        assertEquals("encoded_password", userPrincipal.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals("98765432100", userPrincipal.getUsername());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE")));
    }

    @Test
    void testGetAuthoritiesForAdmin() {
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

        UserPrincipal adminPrincipal = new UserPrincipal(admin);
        Collection<? extends GrantedAuthority> authorities = adminPrincipal.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userPrincipal.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userPrincipal.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userPrincipal.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userPrincipal.isEnabled());
    }

    @Test
    void testCreateStaticMethod() {
        UserPrincipal created = UserPrincipal.create(employee);

        assertNotNull(created);
        assertEquals("98765432100", created.getCpf());
        assertEquals("John Doe", created.getName());
        assertEquals("EMPLOYEE", created.getRole());
    }

    @Test
    void testGetPassword() {
        assertEquals("encoded_password", userPrincipal.getPassword());
    }

    @Test
    void testGetRole() {
        assertEquals("EMPLOYEE", userPrincipal.getRole());
    }

    @Test
    void testGetId() {
        assertEquals(1L, userPrincipal.getId());
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", userPrincipal.getName());
    }

    @Test
    void testUserPrincipalWithDifferentRoles() {
        Employee employee1 = Employee.builder()
                .id(3L)
                .cpf("11111111111")
                .name("User One")
                .pis("22222222222")
                .password("pass1")
                .companyName("Comp1")
                .companyCnpj("11111111111111")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        Employee employee2 = Employee.builder()
                .id(4L)
                .cpf("22222222222")
                .name("User Two")
                .pis("33333333333")
                .password("pass2")
                .companyName("Comp2")
                .companyCnpj("22222222222222")
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        UserPrincipal principal1 = new UserPrincipal(employee1);
        UserPrincipal principal2 = new UserPrincipal(employee2);

        assertEquals("ROLE_EMPLOYEE", principal1.getAuthorities().iterator().next().getAuthority());
        assertEquals("ROLE_ADMIN", principal2.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testUserPrincipalCreatesNewInstance() {
        UserPrincipal principal1 = new UserPrincipal(employee);
        UserPrincipal principal2 = new UserPrincipal(employee);

        assertNotSame(principal1, principal2);
        assertEquals(principal1.getCpf(), principal2.getCpf());
    }
}
