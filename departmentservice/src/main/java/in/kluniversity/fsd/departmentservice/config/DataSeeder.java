package in.kluniversity.fsd.departmentservice.config;

import in.kluniversity.fsd.departmentservice.entity.Department;
import in.kluniversity.fsd.departmentservice.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Seeds a few real departments on first boot so the demo has data immediately. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository repository;

    public DataSeeder(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new Department("Computer Science & Engineering", "CSE", "Dr. R. Sharma", "Block A"));
            repository.save(new Department("Electronics & Communication Engineering", "ECE", "Dr. K. Iyer", "Block B"));
            repository.save(new Department("Mechanical Engineering", "MEC", "Dr. S. Reddy", "Block C"));
            repository.save(new Department("Electrical & Electronics Engineering", "EEE", "Dr. P. Nair", "Block B"));
            repository.save(new Department("Civil Engineering", "CIV", "Dr. M. Rao", "Block D"));
        }
    }
}
