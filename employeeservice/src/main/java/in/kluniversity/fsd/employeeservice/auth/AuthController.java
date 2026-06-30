package in.kluniversity.fsd.employeeservice.auth;

import in.kluniversity.fsd.employeeservice.entity.AppUser;
import in.kluniversity.fsd.employeeservice.repository.AppUserRepository;
import in.kluniversity.fsd.employeeservice.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authManager, JwtService jwtService,
                          AppUserRepository users, PasswordEncoder encoder) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        String role = "ADMIN".equalsIgnoreCase(req.role()) ? "ADMIN" : "USER";
        users.save(new AppUser(req.username(), encoder.encode(req.password()), role));
        return new AuthResponse(jwtService.generateToken(req.username(), role), "Bearer");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        String role = users.findByUsername(req.username()).map(AppUser::getRole).orElse("USER");
        return new AuthResponse(jwtService.generateToken(req.username(), role), "Bearer");
    }
}
