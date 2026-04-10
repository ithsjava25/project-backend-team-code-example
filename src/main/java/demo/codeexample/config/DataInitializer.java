package demo.codeexample.config;

import demo.codeexample.user.domain.Role;
import demo.codeexample.user.domain.User;
import demo.codeexample.user.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... arg) {
        if (userRepository.existsByEmail("admin@filmstudio.com")) {
            return; // admin already exist - do nothing
        }

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("System");
        admin.setEmail("admin@filmstudio.com");
        admin.setPassword(passwordEncoder.encode("ChangeMe123!"));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setPasswordResetRequired(true); // force password change on first login

        userRepository.save(admin);
    }
}
