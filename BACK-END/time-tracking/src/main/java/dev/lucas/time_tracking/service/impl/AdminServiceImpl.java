package dev.lucas.time_tracking.service.impl;

import dev.lucas.time_tracking.dto.DashboardStats;
import dev.lucas.time_tracking.dto.EmployeeRegistrationRequest;
import dev.lucas.time_tracking.dto.EmployeeResponse;
import dev.lucas.time_tracking.dto.TimeRecordResponse;
import dev.lucas.time_tracking.exception.BusinessRuleException;
import dev.lucas.time_tracking.exception.InvalidDateFormatException;
import dev.lucas.time_tracking.exception.ResourceNotFoundException;
import dev.lucas.time_tracking.exception.UserAlreadyExistsException;
import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.Role;
import dev.lucas.time_tracking.model.TimeRecord;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import dev.lucas.time_tracking.repository.TimeRecordRepository;
import dev.lucas.time_tracking.service.AdminService;
import dev.lucas.time_tracking.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final EmployeeRepository employeeRepository;
    private final TimeRecordRepository timeRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    private final String adminCpf;
    private final String adminPassword;

    @Autowired
    public AdminServiceImpl(EmployeeRepository employeeRepository,
                            TimeRecordRepository timeRecordRepository,
                            PasswordEncoder passwordEncoder,
                            AuthenticationService authenticationService,
                            @Value("${admin.cpf}") String adminCpf,
                            @Value("${admin.password}") String adminPassword) {
        this.employeeRepository = employeeRepository;
        this.timeRecordRepository = timeRecordRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.adminCpf = adminCpf;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public EmployeeResponse registerEmployee(EmployeeRegistrationRequest request, String adminCpf) {
        log.info("Admin {} is registering new employee", adminCpf);

        authenticationService.validateAdminCredentials(adminCpf, adminPassword);

        if (employeeRepository.existsByCpf(request.getCpf())) {
            throw new UserAlreadyExistsException("CPF", request.getCpf());
        }

        if (employeeRepository.existsByPis(request.getPis())) {
            throw new UserAlreadyExistsException("PIS", request.getPis());
        }

        Employee employee = Employee.builder()
                .cpf(request.getCpf())
                .name(request.getName())
                .pis(request.getPis())
                .password(passwordEncoder.encode(request.getPassword()))
                .companyName(request.getCompanyName())
                .companyCnpj(request.getCompanyCnpj())
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee registered successfully with CPF: {}", request.getCpf());

        return mapToEmployeeResponse(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(String cpf, EmployeeRegistrationRequest request) {
        log.info("Updating employee with CPF: {}", cpf);

        Employee employee = employeeRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "CPF", cpf));

        if (!cpf.equals(request.getCpf()) && employeeRepository.existsByCpf(request.getCpf())) {
            throw new UserAlreadyExistsException("CPF", request.getCpf());
        }

        if (!employee.getPis().equals(request.getPis()) && employeeRepository.existsByPis(request.getPis())) {
            throw new UserAlreadyExistsException("PIS", request.getPis());
        }

        employee.setCpf(request.getCpf());
        employee.setName(request.getName());
        employee.setPis(request.getPis());
        employee.setCompanyName(request.getCompanyName());
        employee.setCompanyCnpj(request.getCompanyCnpj());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully with CPF: {}", cpf);

        return mapToEmployeeResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(String cpf) {
        log.info("Deleting employee with CPF: {}", cpf);

        if (adminCpf.equals(cpf)) {
            throw new BusinessRuleException("Cannot delete admin user");
        }

        Employee employee = employeeRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "CPF", cpf));

        employeeRepository.delete(employee);
        log.info("Employee deleted successfully with CPF: {}", cpf);
    }

    @Override
    public EmployeeResponse getEmployeeByCpf(String cpf) {
        log.info("Fetching employee with CPF: {}", cpf);

        Employee employee = employeeRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "CPF", cpf));

        return mapToEmployeeResponse(employee);
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        log.info("Fetching all employees with pagination");

        Page<Employee> employeesPage = employeeRepository.findAll(pageable);

        List<EmployeeResponse> employeeResponses = employeesPage.getContent().stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(employeeResponses, pageable, employeesPage.getTotalElements());
    }

    @Override
    public Page<TimeRecordResponse> getAllTimeRecords(Pageable pageable) {
        log.info("Fetching all time records with pagination");

        Page<TimeRecord> recordsPage = timeRecordRepository.findAll(pageable);

        List<TimeRecordResponse> recordResponses = recordsPage.getContent().stream()
                .map(this::mapToTimeRecordResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(recordResponses, pageable, recordsPage.getTotalElements());
    }

    @Override
    public Page<TimeRecordResponse> searchTimeRecordsByDate(String date, Pageable pageable) {
        log.info("Searching time records by date: {}", date);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate searchDate = LocalDate.parse(date, formatter);

            Page<TimeRecord> recordsPage = timeRecordRepository.findByRecordDate(searchDate, pageable);

            return recordsPage.map(this::mapToTimeRecordResponse);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", date);
            throw new InvalidDateFormatException(date, "dd/MM/yyyy");
        }
    }

    @Override
    public DashboardStats getDashboardStats() {
        log.info("Calculating dashboard statistics");

        long totalEmployees = employeeRepository.count();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);

        long totalTimeRecordsToday = timeRecordRepository.countByRecordDate(today);
        long totalTimeRecordsThisWeek = timeRecordRepository.countByRecordDateBetween(startOfWeek, today);
        long totalTimeRecordsThisMonth = timeRecordRepository.countByRecordDateBetween(startOfMonth, today);

        long activeEmployeesToday = timeRecordRepository.countDistinctEmployeesByRecordDate(today);

        LocalDate thirtyDaysAgo = today.minusDays(30);
        long totalRecordsLast30Days = timeRecordRepository.countByRecordDateBetween(thirtyDaysAgo, today);
        double averageRecordsPerDay = totalRecordsLast30Days / 30.0;

        return DashboardStats.builder()
                .totalEmployees(totalEmployees)
                .totalTimeRecordsToday(totalTimeRecordsToday)
                .totalTimeRecordsThisWeek(totalTimeRecordsThisWeek)
                .totalTimeRecordsThisMonth(totalTimeRecordsThisMonth)
                .activeEmployeesToday(activeEmployeesToday)
                .averageRecordsPerDay(averageRecordsPerDay)
                .build();
    }

    @Override
    public List<TimeRecordResponse> getRecentActivity(int limit) {
        log.info("Fetching recent activity, limit: {}", limit);

        Pageable pageable = Pageable.ofSize(limit);
        Page<TimeRecord> recentRecords = timeRecordRepository.findAllByOrderByCreatedAtDesc(pageable);

        return recentRecords.getContent().stream()
                .map(this::mapToTimeRecordResponse)
                .collect(Collectors.toList());
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        long totalTimeRecords = timeRecordRepository.countByEmployee(employee);

        return EmployeeResponse.builder()
                .id(employee.getId())
                .cpf(employee.getCpf())
                .name(employee.getName())
                .pis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .role(employee.getRole().name())
                .enabled(employee.isEnabled())
                .createdAt(employee.getCreatedAt())
                .totalTimeRecords(totalTimeRecords)
                .build();
    }

    private TimeRecordResponse mapToTimeRecordResponse(TimeRecord record) {
        return TimeRecordResponse.builder()
                .id(record.getId())
                .employeeName(record.getEmployeeName())
                .employeePis(record.getEmployeePis())
                .companyName(record.getCompanyName())
                .companyCnpj(record.getCompanyCnpj())
                .recordDate(record.getRecordDate())
                .recordTime(record.getRecordTime())
                .dayOfWeek(record.getDayOfWeek())
                .build();
    }
}
