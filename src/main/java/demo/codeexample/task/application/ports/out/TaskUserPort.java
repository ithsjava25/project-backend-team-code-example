package demo.codeexample.task.application.ports.out;

import demo.codeexample.shared.Role;

public interface TaskUserPort {
    String getEmployeeFullName(Long userId);
    boolean hasRole(Long userId, Role role);
}
