package dev.lucas.time_tracking.service.impl;

import dev.lucas.time_tracking.dto.TimeRecordResponse;
import dev.lucas.time_tracking.exception.ResourceNotFoundException;
import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.TimeRecord;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import dev.lucas.time_tracking.repository.TimeRecordRepository;
import dev.lucas.time_tracking.service.TimeRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeRecordServiceImpl implements TimeRecordService {

    private final TimeRecordRepository timeRecordRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public TimeRecordResponse createTimeRecord(String employeeCpf) {
        log.info("Creating time record for employee with CPF: {}", employeeCpf);

        Employee employee = employeeRepository.findByCpf(employeeCpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with CPF: " + employeeCpf));

        TimeRecord timeRecord = TimeRecord.builder()
                .employee(employee)
                .employeeName(employee.getName())
                .employeePis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .build();

        TimeRecord savedRecord = timeRecordRepository.save(timeRecord);
        log.info("Time record created successfully for employee: {}", employee.getName());

        return mapToResponse(savedRecord);
    }

    @Override
    public Page<TimeRecordResponse> getEmployeeTimeRecords(String employeeCpf, Pageable pageable) {
        log.info("Fetching time records for employee with CPF: {}", employeeCpf);

        Employee employee = employeeRepository.findByCpf(employeeCpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with CPF: " + employeeCpf));

        return timeRecordRepository.findByEmployeeOrderByCreatedAtDesc(employee, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<TimeRecordResponse> searchTimeRecordsByDate(String employeeCpf, String date, Pageable pageable) {
        log.info("Searching time records for employee with CPF: {} and date: {}", employeeCpf, date);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate searchDate = LocalDate.parse(date, formatter);

            return timeRecordRepository.findByEmployeeCpfAndDate(employeeCpf, searchDate, pageable)
                    .map(this::mapToResponse);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", date);
            throw new IllegalArgumentException("Invalid date format. Please use dd/MM/yyyy");
        }
    }

    private TimeRecordResponse mapToResponse(TimeRecord record) {
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
