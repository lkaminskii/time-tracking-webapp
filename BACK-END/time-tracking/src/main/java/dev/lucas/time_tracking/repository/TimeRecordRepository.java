package dev.lucas.time_tracking.repository;

import dev.lucas.time_tracking.model.Employee;
import dev.lucas.time_tracking.model.TimeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    Page<TimeRecord> findByEmployeeOrderByCreatedAtDesc(Employee employee, Pageable pageable);

    @Query("SELECT tr FROM TimeRecord tr WHERE tr.employee = :employee AND tr.recordDate = :date")
    Page<TimeRecord> findByEmployeeAndDate(@Param("employee") Employee employee,
                                           @Param("date") LocalDate date,
                                           Pageable pageable);

    @Query("SELECT tr FROM TimeRecord tr WHERE tr.employee.cpf = :cpf AND tr.recordDate = :date")
    Page<TimeRecord> findByEmployeeCpfAndDate(@Param("cpf") String cpf,
                                              @Param("date") LocalDate date,
                                              Pageable pageable);

    long countByRecordDate(LocalDate date);

    long countByRecordDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT COUNT(DISTINCT tr.employee.id) FROM TimeRecord tr WHERE tr.recordDate = :date")
    long countDistinctEmployeesByRecordDate(@Param("date") LocalDate date);

    long countByEmployee(Employee employee);

    Page<TimeRecord> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<TimeRecord> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT tr FROM TimeRecord tr WHERE tr.recordDate = :date ORDER BY tr.createdAt DESC")
    Page<TimeRecord> findByRecordDate(@Param("date") LocalDate date, Pageable pageable);
}
