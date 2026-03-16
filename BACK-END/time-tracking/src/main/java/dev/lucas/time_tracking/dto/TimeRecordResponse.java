package dev.lucas.time_tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecordResponse {
    private Long id;
    private String employeeName;
    private String employeePis;
    private String companyName;
    private String companyCnpj;
    private LocalDate recordDate;
    private LocalTime recordTime;
    private String dayOfWeek;
}
