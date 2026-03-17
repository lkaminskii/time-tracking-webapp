package dev.lucas.time_tracking.controller;

import dev.lucas.time_tracking.dto.DashboardStats;
import dev.lucas.time_tracking.dto.EmployeeRegistrationRequest;
import dev.lucas.time_tracking.dto.EmployeeResponse;
import dev.lucas.time_tracking.dto.TimeRecordResponse;
import dev.lucas.time_tracking.security.UserPrincipal;
import dev.lucas.time_tracking.service.AdminService;
import dev.lucas.time_tracking.service.EmployeeService;
import dev.lucas.time_tracking.service.TimeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminController {

    private final EmployeeService employeeService;
    private final TimeRecordService timeRecordService;
    private final AdminService adminService;

    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponse> registerEmployee(
            @Valid @RequestBody EmployeeRegistrationRequest request,
            @AuthenticationPrincipal UserPrincipal adminUser) {

        log.info("Admin {} is registering new employee with CPF: {}", adminUser.getCpf(), request.getCpf());
        EmployeeResponse response = adminService.registerEmployee(request, adminUser.getCpf());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Admin {} is fetching all employees", adminUser.getCpf());
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<EmployeeResponse> employees = adminService.getAllEmployees(pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{cpf}")
    public ResponseEntity<EmployeeResponse> getEmployeeByCpf(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @PathVariable String cpf) {

        log.info("Admin {} is fetching employee with CPF: {}", adminUser.getCpf(), cpf);
        EmployeeResponse employee = adminService.getEmployeeByCpf(cpf);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/employees/{cpf}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @PathVariable String cpf,
            @Valid @RequestBody EmployeeRegistrationRequest request) {

        log.info("Admin {} is updating employee with CPF: {}", adminUser.getCpf(), cpf);
        EmployeeResponse response = adminService.updateEmployee(cpf, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/employees/{cpf}")
    public ResponseEntity<Void> deleteEmployee(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @PathVariable String cpf) {

        log.info("Admin {} is deleting employee with CPF: {}", adminUser.getCpf(), cpf);
        adminService.deleteEmployee(cpf);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees/{cpf}/time-records")
    public ResponseEntity<Page<TimeRecordResponse>> getEmployeeTimeRecords(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @PathVariable String cpf,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Admin {} is fetching time records for employee: {}", adminUser.getCpf(), cpf);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = timeRecordService.getEmployeeTimeRecords(cpf, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/time-records/all")
    public ResponseEntity<Page<TimeRecordResponse>> getAllTimeRecords(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Admin {} is fetching all time records", adminUser.getCpf());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = adminService.getAllTimeRecords(pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/time-records/search")
    public ResponseEntity<Page<TimeRecordResponse>> searchTimeRecordsByDate(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Admin {} is searching time records by date: {}", adminUser.getCpf(), date);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = adminService.searchTimeRecordsByDate(date, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/time-records/employee/{cpf}/search")
    public ResponseEntity<Page<TimeRecordResponse>> searchEmployeeTimeRecordsByDate(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @PathVariable String cpf,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Admin {} is searching time records for employee {} by date: {}", adminUser.getCpf(), cpf, date);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = timeRecordService.searchTimeRecordsByDate(cpf, date, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStats> getDashboardStats(@AuthenticationPrincipal UserPrincipal adminUser) {
        log.info("Admin {} is fetching dashboard statistics", adminUser.getCpf());
        DashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dashboard/recent-activity")
    public ResponseEntity<List<TimeRecordResponse>> getRecentActivity(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @RequestParam(defaultValue = "10") int limit) {

        log.info("Admin {} is fetching recent activity", adminUser.getCpf());
        List<TimeRecordResponse> recentActivity = adminService.getRecentActivity(limit);
        return ResponseEntity.ok(recentActivity);
    }
}
