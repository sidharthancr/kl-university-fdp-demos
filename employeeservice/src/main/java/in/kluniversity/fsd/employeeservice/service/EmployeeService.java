package in.kluniversity.fsd.employeeservice.service;

import in.kluniversity.fsd.employeeservice.dto.EmployeePatchRequest;
import in.kluniversity.fsd.employeeservice.entity.Employee;
import in.kluniversity.fsd.employeeservice.exception.EmployeeNotFoundException;
import in.kluniversity.fsd.employeeservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The Service layer: holds business logic and sits between the Controller and the Repository.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee create(Employee employee) {
        // Ignore any client-supplied id so POST always creates a new row.
        employee.setId(null);
        return repository.save(employee);
    }

    public Employee update(Long id, Employee changes) {
        Employee existing = findById(id);
        existing.setFirstName(changes.getFirstName());
        existing.setLastName(changes.getLastName());
        existing.setEmail(changes.getEmail());
        existing.setJobTitle(changes.getJobTitle());
        existing.setDepartmentId(changes.getDepartmentId());
        return repository.save(existing);
    }

    /**
     * Partial update: only the non-null fields from the request are changed.
     */
    public Employee patch(Long id, EmployeePatchRequest changes) {
        Employee existing = findById(id);
        if (changes.getFirstName() != null) {
            existing.setFirstName(changes.getFirstName());
        }
        if (changes.getLastName() != null) {
            existing.setLastName(changes.getLastName());
        }
        if (changes.getEmail() != null) {
            existing.setEmail(changes.getEmail());
        }
        if (changes.getJobTitle() != null) {
            existing.setJobTitle(changes.getJobTitle());
        }
        if (changes.getDepartmentId() != null) {
            existing.setDepartmentId(changes.getDepartmentId());
        }
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }
}

