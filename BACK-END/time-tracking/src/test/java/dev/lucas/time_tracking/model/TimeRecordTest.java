package dev.lucas.time_tracking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class TimeRecordTest {

    private TimeRecord timeRecord;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .cpf("98765432100")
                .name("John Doe")
                .pis("12345678900")
                .password("password")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        timeRecord = TimeRecord.builder()
                .id(1L)
                .employee(employee)
                .employeeName("John Doe")
                .employeePis("12345678900")
                .companyName("Company")
                .companyCnpj("12345678901234")
                .recordDate(LocalDate.now())
                .recordTime(LocalTime.now())
                .dayOfWeek("MONDAY")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testTimeRecordConstructorWithBuilder() {
        assertNotNull(timeRecord);
        assertEquals(1L, timeRecord.getId());
        assertEquals(employee, timeRecord.getEmployee());
        assertEquals("John Doe", timeRecord.getEmployeeName());
    }

    @Test
    void testTimeRecordGettersAndSetters() {
        timeRecord.setEmployeeName("Jane Smith");
        timeRecord.setEmployeePis("11111111111");
        timeRecord.setCompanyName("New Company");
        timeRecord.setDayOfWeek("TUESDAY");

        assertEquals("Jane Smith", timeRecord.getEmployeeName());
        assertEquals("11111111111", timeRecord.getEmployeePis());
        assertEquals("New Company", timeRecord.getCompanyName());
        assertEquals("TUESDAY", timeRecord.getDayOfWeek());
    }

    @Test
    void testTimeRecordWithDifferentDaysOfWeek() {
        String[] daysOfWeek = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        for (String day : daysOfWeek) {
            timeRecord.setDayOfWeek(day);
            assertEquals(day, timeRecord.getDayOfWeek());
        }
    }

    @Test
    void testTimeRecordRecordDate() {
        LocalDate testDate = LocalDate.of(2024, 1, 15);
        timeRecord.setRecordDate(testDate);

        assertEquals(testDate, timeRecord.getRecordDate());
    }

    @Test
    void testTimeRecordRecordTime() {
        LocalTime testTime = LocalTime.of(10, 30, 45);
        timeRecord.setRecordTime(testTime);

        assertEquals(testTime, timeRecord.getRecordTime());
    }

    @Test
    void testTimeRecordCreationTimestamp() {
        LocalDateTime createdAt = LocalDateTime.now();
        timeRecord.setCreatedAt(createdAt);

        assertNotNull(timeRecord.getCreatedAt());
        assertEquals(createdAt, timeRecord.getCreatedAt());
    }

    @Test
    void testTimeRecordEmployeeReference() {
        Employee newEmployee = Employee.builder()
                .id(2L)
                .cpf("11111111111")
                .name("Jane Smith")
                .pis("22222222222")
                .password("password")
                .companyName("Other Company")
                .companyCnpj("99999999999999")
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        timeRecord.setEmployee(newEmployee);

        assertEquals(newEmployee, timeRecord.getEmployee());
        assertEquals("11111111111", timeRecord.getEmployee().getCpf());
    }

    @Test
    void testTimeRecordNoArgsConstructor() {
        TimeRecord emptyRecord = new TimeRecord();
        assertNotNull(emptyRecord);
        assertNull(emptyRecord.getId());
    }

    @Test
    void testTimeRecordAllArgsConstructor() {
        LocalDate recordDate = LocalDate.now();
        LocalTime recordTime = LocalTime.now();
        LocalDateTime createdAt = LocalDateTime.now();

        TimeRecord record = new TimeRecord(
                1L,
                employee,
                "John Doe",
                "12345678900",
                "Company",
                "12345678901234",
                recordDate,
                recordTime,
                "MONDAY",
                createdAt
        );

        assertEquals(1L, record.getId());
        assertEquals("John Doe", record.getEmployeeName());
    }

    @Test
    void testTimeRecordConsistencyOfData() {
        assertEquals(employee.getName(), timeRecord.getEmployeeName());
        assertEquals(employee.getPis(), timeRecord.getEmployeePis());
        assertEquals(employee.getCompanyName(), timeRecord.getCompanyName());
        assertEquals(employee.getCompanyCnpj(), timeRecord.getCompanyCnpj());
    }

    @Test
    void testTimeRecordWithMultipleRecords() {
        TimeRecord record1 = TimeRecord.builder()
                .id(1L)
                .employee(employee)
                .employeeName("John")
                .recordDate(LocalDate.now())
                .build();

        TimeRecord record2 = TimeRecord.builder()
                .id(2L)
                .employee(employee)
                .employeeName("Jane")
                .recordDate(LocalDate.now().minusDays(1))
                .build();

        assertNotEquals(record1.getId(), record2.getId());
        assertNotEquals(record1.getEmployeeName(), record2.getEmployeeName());
    }

    @Test
    void testTimeRecordTimestampNotNull() {
        assertNotNull(timeRecord.getCreatedAt());
    }

    @Test
    void testTimeRecordCompanyDataIntegrity() {
        assertEquals("12345678901234", timeRecord.getCompanyCnpj());
        assertEquals("Company", timeRecord.getCompanyName());
    }

    @Test
    void testTimeRecordDateTimeFields() {
        LocalDate date = LocalDate.of(2024, 3, 16);
        LocalTime time = LocalTime.of(14, 30, 0);

        timeRecord.setRecordDate(date);
        timeRecord.setRecordTime(time);

        assertEquals(date, timeRecord.getRecordDate());
        assertEquals(time, timeRecord.getRecordTime());
    }
}
