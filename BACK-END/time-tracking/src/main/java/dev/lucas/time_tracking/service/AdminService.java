package dev.lucas.time_tracking.service;

import dev.lucas.time_tracking.dto.DashboardStats;
import dev.lucas.time_tracking.dto.EmployeeRegistrationRequest;
import dev.lucas.time_tracking.dto.EmployeeResponse;
import dev.lucas.time_tracking.dto.TimeRecordResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    EmployeeResponse registerEmployee(EmployeeRegistrationRequest request, String adminCpf);
    EmployeeResponse updateEmployee(String cpf, EmployeeRegistrationRequest request);
    void deleteEmployee(String cpf);
    EmployeeResponse getEmployeeByCpf(String cpf);
    Page<EmployeeResponse> getAllEmployees(Pageable pageable);
    Page<TimeRecordResponse> getAllTimeRecords(Pageable pageable);
    Page<TimeRecordResponse> searchTimeRecordsByDate(String date, Pageable pageable);
    DashboardStats getDashboardStats();
    List<TimeRecordResponse> getRecentActivity(int limit);
}
