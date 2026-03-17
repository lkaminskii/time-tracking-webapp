package dev.lucas.time_tracking.controller;

import dev.lucas.time_tracking.dto.TimeRecordResponse;
import dev.lucas.time_tracking.security.UserPrincipal;
import dev.lucas.time_tracking.service.TimeRecordService;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping("/time-records")
    public ResponseEntity<TimeRecordResponse> createTimeRecord(@AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("Creating time record for employee: {}", currentUser.getCpf());
        TimeRecordResponse response = timeRecordService.createTimeRecord(currentUser.getCpf());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/time-records")
    public ResponseEntity<Page<TimeRecordResponse>> getTimeRecords(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching time records for employee: {}", currentUser.getCpf());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = timeRecordService.getEmployeeTimeRecords(currentUser.getCpf(), pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/time-records/search")
    public ResponseEntity<Page<TimeRecordResponse>> searchTimeRecords(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Searching time records for employee: {} with date: {}", currentUser.getCpf(), date);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = timeRecordService.searchTimeRecordsByDate(currentUser.getCpf(), date, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/time-records/today")
    public ResponseEntity<Page<TimeRecordResponse>> getTodayRecords(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching today's time records for employee: {}", currentUser.getCpf());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        LocalDate today = LocalDate.now();
        String dateStr = String.format("%02d/%02d/%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear());
        Page<TimeRecordResponse> records = timeRecordService.searchTimeRecordsByDate(currentUser.getCpf(), dateStr, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/time-records/latest")
    public ResponseEntity<TimeRecordResponse> getLatestTimeRecord(@AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("Fetching latest time record for employee: {}", currentUser.getCpf());
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        Page<TimeRecordResponse> records = timeRecordService.getEmployeeTimeRecords(currentUser.getCpf(), pageable);

        if (records.hasContent()) {
            return ResponseEntity.ok(records.getContent().get(0));
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
