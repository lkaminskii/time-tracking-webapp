package dev.lucas.time_tracking.service;

import dev.lucas.time_tracking.dto.EmployeeRegistrationRequest;
import dev.lucas.time_tracking.dto.JwtAuthenticationResponse;
import dev.lucas.time_tracking.dto.LoginRequest;

public interface EmployeeService {
    JwtAuthenticationResponse authenticate(LoginRequest loginRequest);
    EmployeeRegistrationRequest registerEmployee(EmployeeRegistrationRequest request, String adminCpf);
    EmployeeRegistrationRequest getEmployeeByCpf(String cpf);
}
