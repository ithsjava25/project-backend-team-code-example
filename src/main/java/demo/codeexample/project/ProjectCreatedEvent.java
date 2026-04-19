package demo.codeexample.project;

import java.time.LocalDate;
import java.util.Set;

public record ProjectCreatedEvent(
        Long projectId,
        String title,
        Set<Long> employeeId,
        LocalDate ,
        Long companyId
) {}
