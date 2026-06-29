package in.kluniversity.fsd.employeeservice.config;

import in.kluniversity.fsd.employeeservice.entity.AppUser;
import in.kluniversity.fsd.employeeservice.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Seeds admin/admin123 and user/user123 so logins work out of the box. */
@Component
public class UserSeeder implements CommandLineRunner {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public UserSeeder(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            users.save(new AppUser("admin", encoder.encode("admin123"), "ADMIN"));
            users.save(new AppUser("user", encoder.encode("user123"), "USER"));
        }
    }
}
