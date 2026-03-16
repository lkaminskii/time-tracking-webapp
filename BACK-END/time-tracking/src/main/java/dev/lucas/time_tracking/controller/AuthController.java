package dev.lucas.time_tracking.controller;

import dev.lucas.time_tracking.dto.JwtAuthenticationResponse;
import dev.lucas.time_tracking.dto.LoginRequest;
import dev.lucas.time_tracking.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for CPF: {}", loginRequest.getCpf());
        JwtAuthenticationResponse response = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
}
