package demo.codeexample.config;

import demo.codeexample.user.CreateUserRequestDTO;
import demo.codeexample.shared.Role;
import demo.codeexample.user.UserLookup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UserLookup userLookup;

    public DataInitializer(UserLookup userLookup) {
        this.userLookup = userLookup;
    }

    @Override
    public void run(String... arg) {

        // Check if admin exists via public interface
        if (userLookup.findByEmail("admin@filmstudio.com").isPresent()) {
            return; // admin already exists — do nothing
        }

        CreateUserRequestDTO adminRequest = new CreateUserRequestDTO();
        adminRequest.setFirstName("Admin");
        adminRequest.setLastName("System");
        adminRequest.setEmail("admin@filmstudio.com");
        adminRequest.setRole(Role.ADMIN);
        // No password set — createUser() generates temp password automatically!

        userLookup.createUser(adminRequest);

        System.out.println("========================================");
        System.out.println("  Default admin created!");
        System.out.println("  Email:    admin@filmstudio.com");
        System.out.println("  Check Mailtrap for temp password!");
        System.out.println("========================================");
    }
}
