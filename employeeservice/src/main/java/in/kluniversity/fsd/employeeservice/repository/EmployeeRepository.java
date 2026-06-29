package in.kluniversity.fsd.employeeservice.repository;

import in.kluniversity.fsd.employeeservice.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The Repository layer: Spring Data JPA generates the implementation at runtime.
 * Extending JpaRepository gives us findAll/findById/save/deleteById/count for free.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);
}
