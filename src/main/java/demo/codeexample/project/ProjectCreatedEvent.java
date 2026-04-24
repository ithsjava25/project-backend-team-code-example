package demo.codeexample.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record ProjectCreatedEvent(
        Long projectId,
        String title,
        Set<Long> employeesId,
        LocalDate releaseDate,
        String companyName,
        LocalDateTime recruitingDeadline,
        LocalDateTime recordingDeadline,
        LocalDateTime editingDeadline
) {}
