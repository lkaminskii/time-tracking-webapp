package dev.lucas.time_tracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "time_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "FK_time_records_employee"))
    @JsonIgnore
    private Employee employee;

    @Column(name = "employee_name", nullable = false, length = 100)
    private String employeeName;

    @Column(name = "employee_pis", nullable = false, length = 20)
    private String employeePis;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "company_cnpj", nullable = false, length = 18)
    private String companyCnpj;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "record_time", nullable = false)
    private LocalTime recordTime;

    @Column(name = "day_of_week", nullable = false, length = 20)
    private String dayOfWeek;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        recordDate = LocalDate.now();
        recordTime = LocalTime.now();
        dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    }
}