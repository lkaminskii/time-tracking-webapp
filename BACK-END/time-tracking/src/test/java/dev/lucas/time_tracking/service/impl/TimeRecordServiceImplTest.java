package dev.lucas.time_tracking.service.impl;

import dev.lucas.time_tracking.dto.TimeRecordResponse;
import dev.lucas.time_tracking.exception.ResourceNotFoundException;
import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.Role;
import dev.lucas.time_tracking.model.TimeRecord;
import dev.lucas.time_tracking.repository.EmployeeRepository;
import dev.lucas.time_tracking.repository.TimeRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceImplTest {

    @Mock
    private TimeRecordRepository timeRecordRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TimeRecordServiceImpl timeRecordService;

    private Employee employee;
    private TimeRecord timeRecord;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .cpf("98765432100")
                .name("John Doe")
                .pis("12345678900")
                .password("encoded_password")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        timeRecord = TimeRecord.builder()
                .id(1L)
                .employee(employee)
                .employeeName(employee.getName())
                .employeePis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .recordDate(LocalDate.now())
                .recordTime(LocalTime.now())
                .dayOfWeek("MONDAY")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateTimeRecordSuccess() {
        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(timeRecordRepository.save(any())).thenReturn(timeRecord);

        TimeRecordResponse response = timeRecordService.createTimeRecord("98765432100");

        assertNotNull(response);
        assertEquals(employee.getName(), response.getEmployeeName());
        assertEquals(employee.getPis(), response.getEmployeePis());
        assertEquals(employee.getCompanyName(), response.getCompanyName());
        assertEquals(employee.getCompanyCnpj(), response.getCompanyCnpj());
        verify(employeeRepository, times(1)).findByCpf("98765432100");
        verify(timeRecordRepository, times(1)).save(any());
    }

    @Test
    void testCreateTimeRecordEmployeeNotFound() {
        when(employeeRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> timeRecordService.createTimeRecord("99999999999"));

        assertTrue(exception.getMessage().contains("Employee not found"));
    }

    @Test
    void testCreateTimeRecordWithPersistentData() {
        TimeRecord savedRecord = TimeRecord.builder()
                .id(1L)
                .employee(employee)
                .employeeName("John Doe")
                .employeePis("12345678900")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .recordDate(LocalDate.now())
                .recordTime(LocalTime.of(10, 30, 45))
                .dayOfWeek("MONDAY")
                .createdAt(LocalDateTime.now())
                .build();

        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(timeRecordRepository.save(any())).thenReturn(savedRecord);

        TimeRecordResponse response = timeRecordService.createTimeRecord("98765432100");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getEmployeeName());
        assertEquals("12345678900", response.getEmployeePis());
        assertEquals(LocalDate.now(), response.getRecordDate());
    }

    @Test
    void testGetEmployeeTimeRecordsSuccess() {
        TimeRecord record2 = TimeRecord.builder()
                .id(2L)
                .employee(employee)
                .employeeName(employee.getName())
                .employeePis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .recordDate(LocalDate.now().minusDays(1))
                .recordTime(LocalTime.now())
                .dayOfWeek("SUNDAY")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        List<TimeRecord> records = Arrays.asList(timeRecord, record2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeRecord> recordsPage = new PageImpl<>(records, pageable, 2);

        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(timeRecordRepository.findByEmployeeOrderByCreatedAtDesc(employee, pageable))
                .thenReturn(recordsPage);

        Page<TimeRecordResponse> response = timeRecordService.getEmployeeTimeRecords("98765432100", pageable);

        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
        assertEquals(2, response.getContent().size());
        verify(employeeRepository, times(1)).findByCpf("98765432100");
        verify(timeRecordRepository, times(1)).findByEmployeeOrderByCreatedAtDesc(employee, pageable);
    }

    @Test
    void testGetEmployeeTimeRecordsEmployeeNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(employeeRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> timeRecordService.getEmployeeTimeRecords("99999999999", pageable));

        assertTrue(exception.getMessage().contains("Employee not found"));
    }

    @Test
    void testGetEmployeeTimeRecordsEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeRecord> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);

        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(timeRecordRepository.findByEmployeeOrderByCreatedAtDesc(employee, pageable))
                .thenReturn(emptyPage);

        Page<TimeRecordResponse> response = timeRecordService.getEmployeeTimeRecords("98765432100", pageable);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    void testGetEmployeeTimeRecordsWithDifferentPages() {
        TimeRecord record2 = TimeRecord.builder()
                .id(2L)
                .employee(employee)
                .employeeName(employee.getName())
                .employeePis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .recordDate(LocalDate.now().minusDays(1))
                .recordTime(LocalTime.now())
                .dayOfWeek("SUNDAY")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        List<TimeRecord> page1Records = Arrays.asList(timeRecord);
        Pageable pageable1 = PageRequest.of(0, 1);
        Page<TimeRecord> page1 = new PageImpl<>(page1Records, pageable1, 2);

        when(employeeRepository.findByCpf("98765432100")).thenReturn(Optional.of(employee));
        when(timeRecordRepository.findByEmployeeOrderByCreatedAtDesc(employee, pageable1))
                .thenReturn(page1);

        Page<TimeRecordResponse> response = timeRecordService.getEmployeeTimeRecords("98765432100", pageable1);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(2, response.getTotalElements());
    }

    @Test
    void testSearchTimeRecordsByDateSuccess() {
        TimeRecord record2 = TimeRecord.builder()
                .id(2L)
                .employee(employee)
                .employeeName(employee.getName())
                .employeePis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .recordDate(LocalDate.now())
                .recordTime(LocalTime.of(14, 30))
                .dayOfWeek("MONDAY")
                .createdAt(LocalDateTime.now())
                .build();

        List<TimeRecord> records = Arrays.asList(timeRecord, record2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeRecord> recordsPage = new PageImpl<>(records, pageable, 2);

        LocalDate searchDate = LocalDate.now();
        when(timeRecordRepository.findByEmployeeCpfAndDate("98765432100", searchDate, pageable))
                .thenReturn(recordsPage);

        Page<TimeRecordResponse> response = timeRecordService.searchTimeRecordsByDate("98765432100",
                String.format("%02d/%02d/%d", searchDate.getDayOfMonth(),
                        searchDate.getMonthValue(), searchDate.getYear()), pageable);

        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
        verify(timeRecordRepository, times(1)).findByEmployeeCpfAndDate("98765432100", searchDate, pageable);
    }

    @Test
    void testSearchTimeRecordsByDateInvalidFormat() {
        Pageable pageable = PageRequest.of(0, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> timeRecordService.searchTimeRecordsByDate("98765432100", "invalid-date", pageable));

        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    void testSearchTimeRecordsByDateEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeRecord> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);

        LocalDate searchDate = LocalDate.now();
        when(timeRecordRepository.findByEmployeeCpfAndDate("98765432100", searchDate, pageable))
                .thenReturn(emptyPage);

        Page<TimeRecordResponse> response = timeRecordService.searchTimeRecordsByDate("98765432100",
                String.format("%02d/%02d/%d", searchDate.getDayOfMonth(),
                        searchDate.getMonthValue(), searchDate.getYear()), pageable);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
    }

    @Test
    void testSearchTimeRecordsByDateDifferentMonths() {
        TimeRecord oldRecord = TimeRecord.builder()
                .id(3L)
                .employee(employee)
                .employeeName(employee.getName())
                .employeePis(employee.getPis())
                .companyName(employee.getCompanyName())
                .companyCnpj(employee.getCompanyCnpj())
                .recordDate(LocalDate.of(2024, 1, 15))
                .recordTime(LocalTime.now())
                .dayOfWeek("MONDAY")
                .createdAt(LocalDateTime.now())
                .build();

        List<TimeRecord> records = Arrays.asList(oldRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeRecord> recordsPage = new PageImpl<>(records, pageable, 1);

        LocalDate searchDate = LocalDate.of(2024, 1, 15);
        when(timeRecordRepository.findByEmployeeCpfAndDate("98765432100", searchDate, pageable))
                .thenReturn(recordsPage);

        Page<TimeRecordResponse> response = timeRecordService.searchTimeRecordsByDate("98765432100",
                "15/01/2024", pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(LocalDate.of(2024, 1, 15), response.getContent().get(0).getRecordDate());
    }
}
