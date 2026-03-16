package dev.lucas.time_tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalEmployees;
    private long totalTimeRecordsToday;
    private long totalTimeRecordsThisWeek;
    private long totalTimeRecordsThisMonth;
    private long activeEmployeesToday;
    private double averageRecordsPerDay;
}
