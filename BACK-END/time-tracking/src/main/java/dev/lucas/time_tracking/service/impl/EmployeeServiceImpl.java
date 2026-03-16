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
import dev.lucas.time_tracking.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationService authenticationService;

    @Value("${admin.cpf}")
    private String adminCpf;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public JwtAuthenticationResponse authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user with CPF: {}", loginRequest.getCpf());

        try {
            if (adminCpf != null && adminCpf.equals(loginRequest.getCpf())) {
                return authenticationService.authenticate(loginRequest);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCpf(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Employee employee = employeeRepository.findByCpf(loginRequest.getCpf())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            String jwt = tokenProvider.generateToken(authentication, loginRequest.isRememberMe());

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .cpf(employee.getCpf())
                    .name(employee.getName())
                    .role(employee.getRole().name())
                    .build();

        } catch (Exception e) {
            log.error("Authentication failed for CPF: {}", loginRequest.getCpf());
            throw new UnauthorizedException("Invalid CPF or password");
        }
    }

    @Override
    @Transactional
    public EmployeeRegistrationRequest registerEmployee(EmployeeRegistrationRequest request, String adminCpf) {
        log.info("Admin with CPF: {} is registering new employee", adminCpf);

        authenticationService.validateAdminCredentials(adminCpf, adminPassword);

        if (employeeRepository.existsByCpf(request.getCpf())) {
            throw new UserAlreadyExistsException("Employee with CPF " + request.getCpf() + " already exists");
        }

        if (employeeRepository.existsByPis(request.getPis())) {
            throw new UserAlreadyExistsException("Employee with PIS " + request.getPis() + " already exists");
        }

        Employee employee = Employee.builder()
                .cpf(request.getCpf())
                .name(request.getName())
                .pis(request.getPis())
                .password(passwordEncoder.encode(request.getPassword()))
                .companyName(request.getCompanyName())
                .companyCnpj(request.getCompanyCnpj())
                .role(Role.EMPLOYEE)
                .build();

        employeeRepository.save(employee);
        log.info("Employee registered successfully with CPF: {}", request.getCpf());

        return request;
    }

    @Override
    public EmployeeRegistrationRequest getEmployeeByCpf(String cpf) {
        Employee employee = employeeRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with CPF: " + cpf));

        EmployeeRegistrationRequest request = new EmployeeRegistrationRequest();
        request.setCpf(employee.getCpf());
        request.setName(employee.getName());
        request.setPis(employee.getPis());
        request.setCompanyName(employee.getCompanyName());
        request.setCompanyCnpj(employee.getCompanyCnpj());

        return request;
    }
}