package in.kluniversity.fsd.employeeservice.config;

import in.kluniversity.fsd.employeeservice.entity.Employee;
import in.kluniversity.fsd.employeeservice.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds sample employees on startup (only when the table is empty) so the very
 * first GET in the demo already returns data. Teaching convenience only.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository repository;

    public DataSeeder(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new Employee("Ada", "Lovelace", "ada.lovelace@klu.edu", "Professor", null));
            repository.save(new Employee("Alan", "Turing", "alan.turing@klu.edu", "Researcher", null));
            repository.save(new Employee("Grace", "Hopper", "grace.hopper@klu.edu", "Professor", null));
            repository.save(new Employee("Dennis", "Ritchie", "dennis.ritchie@klu.edu", "Lecturer", null));
            repository.save(new Employee("Margaret", "Hamilton", "margaret.hamilton@klu.edu", "Engineer", null));
            repository.save(new Employee("Linus", "Torvalds", "linus.torvalds@klu.edu", "Engineer", null));
        }
    }
}
