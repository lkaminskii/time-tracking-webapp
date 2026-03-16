package dev.lucas.time_tracking.service;

import dev.lucas.time_tracking.dto.TimeRecordResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TimeRecordService {
    TimeRecordResponse createTimeRecord(String employeeCpf);
    Page<TimeRecordResponse> getEmployeeTimeRecords(String employeeCpf, Pageable pageable);
    Page<TimeRecordResponse> searchTimeRecordsByDate(String employeeCpf, String date, Pageable pageable);
}
