package dev.lucas.time_tracking.security;

import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with CPF: " + cpf));

        return UserPrincipal.create(employee);
    }
}
