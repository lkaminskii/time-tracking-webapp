package dev.lucas.time_tracking.service;

import dev.lucas.time_tracking.dto.JwtAuthenticationResponse;
import dev.lucas.time_tracking.dto.LoginRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse authenticate(LoginRequest loginRequest);
    void validateAdminCredentials(String cpf, String password);
}
