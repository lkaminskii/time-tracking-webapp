package dev.lucas.time_tracking.service.impl;

import dev.lucas.time_tracking.dto.JwtAuthenticationResponse;
import dev.lucas.time_tracking.dto.LoginRequest;
import dev.lucas.time_tracking.exception.InvalidCredentialsException;
import dev.lucas.time_tracking.exception.UnauthorizedException;
import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import dev.lucas.time_tracking.security.JwtTokenProvider;
import dev.lucas.time_tracking.security.UserPrincipal;
import dev.lucas.time_tracking.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.cpf}")
    private String adminCpf;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public JwtAuthenticationResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user with CPF: {}", loginRequest.getCpf());

        try {
            if (adminCpf.equals(loginRequest.getCpf())) {
                return handleAdminAuthentication(loginRequest);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCpf(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Employee employee = employeeRepository.findByCpf(loginRequest.getCpf())
                    .orElseThrow(() -> new InvalidCredentialsException("Employee not found"));

            String jwt = tokenProvider.generateToken(authentication, loginRequest.isRememberMe());

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .cpf(employee.getCpf())
                    .name(employee.getName())
                    .role(employee.getRole().name())
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Authentication failed for CPF: {} - Invalid credentials", loginRequest.getCpf());
            throw new InvalidCredentialsException("Invalid CPF or password");
        } catch (Exception e) {
            log.error("Authentication failed for CPF: {}", loginRequest.getCpf(), e);
            throw new InvalidCredentialsException("Authentication failed");
        }
    }

    @Override
    public void validateAdminCredentials(String cpf, String password) {
        log.info("Validating admin credentials for CPF: {}", cpf);

        if (!adminCpf.equals(cpf) || !adminPassword.equals(password)) {
            log.error("Invalid admin credentials for CPF: {}", cpf);
            throw new UnauthorizedException("Invalid admin credentials");
        }
    }

    private JwtAuthenticationResponse handleAdminAuthentication(LoginRequest loginRequest) {
        if (!adminPassword.equals(loginRequest.getPassword())) {
            throw new InvalidCredentialsException("Invalid admin password");
        }
        
        Employee admin = employeeRepository.findByCpf(adminCpf).orElse(null);
        if (admin == null) {
            admin = Employee.builder()
                    .cpf(adminCpf)
                    .name("Administrator")
                    .password(passwordEncoder.encode(adminPassword))
                    .role(dev.lucas.time_tracking.model.Role.ADMIN)
                    .companyName("System Admin")
                    .companyCnpj("00000000000000")
                    .pis("00000000000")
                    .build();
            admin = employeeRepository.save(admin);
        }

        UserPrincipal userPrincipal = new UserPrincipal(admin);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication, loginRequest.isRememberMe());

        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .cpf(admin.getCpf())
                .name(admin.getName())
                .role(admin.getRole().name())
                .build();
    }
}
