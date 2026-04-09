package demo.codeexample.config;

import demo.codeexample.enums.Role;
import demo.codeexample.user.User;
import demo.codeexample.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/* Why CommandLineRunner? It's a Spring Boot interface with one method — run().
    Spring calls it automatically after the application context is fully loaded.
    Perfect for setup tasks.*/

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*Why check existsByEmail first?
    Every time you restart the application, this runs again.
    Without the check, it would try to create a duplicate admin and crash on the unique email constraint.*/

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

        /*Why passwordResetRequired = true?
        The hardcoded password "ChangeMe123!" is in your source code — potentially visible in version control.
        Forcing a reset on first login means this temporary password has a very short life.*/

        userRepository.save(admin);

        System.out.println("========================================");
        System.out.println("  Default admin created!");
        System.out.println("  Email:    admin@filmstudio.com");
        System.out.println("  Password: ChangeMe123!");
        System.out.println("  CHANGE THIS PASSWORD IMMEDIATELY!");
        System.out.println("========================================");

    }
}
