package dev.lucas.time_tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String cpf;
    private String name;
    private String pis;
    private String companyName;
    private String companyCnpj;
    private String role;
    private boolean enabled;
    private LocalDateTime createdAt;
    private Long totalTimeRecords;
}
