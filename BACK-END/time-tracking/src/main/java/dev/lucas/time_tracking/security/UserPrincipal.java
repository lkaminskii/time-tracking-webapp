package dev.lucas.time_tracking.security;

import dev.lucas.time_tracking.model.Employee;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String cpf;
    private final String name;
    private final String password;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Employee employee) {
        this.id = employee.getId();
        this.cpf = employee.getCpf();
        this.name = employee.getName();
        this.password = employee.getPassword();
        this.role = employee.getRole().name();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + employee.getRole().name())
        );
    }

    public static UserPrincipal create(Employee employee) {
        return new UserPrincipal(employee);
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}