package demo.codeexample.task.infrastructure.adapters.in;

import demo.codeexample.exceptions.UserNotFoundException;
import demo.codeexample.shared.Role;
import demo.codeexample.task.application.ports.out.TaskUserPort;
import demo.codeexample.user.UserLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskUserAdapter implements TaskUserPort {

    private final UserLookup userLookup; // Modulith dependency is isolated here

    @Override
    public String getEmployeeFullName(Long userId) {
        return userLookup.findById(userId)
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown User");
    }

    @Override
    public boolean hasRole(Long userId, Role role) {
        try {
            return userLookup.validateUserRole(userId, role);
        } catch (UserNotFoundException ex) {
            return false;
        }
    }
}
