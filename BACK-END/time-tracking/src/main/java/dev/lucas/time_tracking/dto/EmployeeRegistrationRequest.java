package dev.lucas.time_tracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeRegistrationRequest {

    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}", message = "CPF must contain exactly 11 digits")
    private String cpf;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "PIS is required")
    @Pattern(regexp = "\\d{11}", message = "PIS must contain exactly 11 digits")
    private String pis;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Company CNPJ is required")
    @Pattern(regexp = "\\d{14}", message = "CNPJ must contain exactly 14 digits")
    private String companyCnpj;
}