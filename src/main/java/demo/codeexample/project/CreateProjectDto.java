package demo.codeexample.project;

import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record CreateProjectDto(
        String title,
        String description,
        LocalDate releaseDate,
        Set<Long> employeesId,
        Category category,
        Genre genre,
        Long fileId,
        Long companyId,
        LocalDateTime recruitingDeadline,
        LocalDateTime recordingDeadline,
        LocalDateTime editingDeadline
) {}



