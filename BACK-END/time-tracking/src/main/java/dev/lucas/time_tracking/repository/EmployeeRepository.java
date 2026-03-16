package dev.lucas.time_tracking.repository;

import dev.lucas.time_tracking.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    boolean existsByPis(String pis);
}
