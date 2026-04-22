package demo.codeexample.project;

import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.shared.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record CreateProjectDto(
        @NotBlank(message = "Project must have a title.")
        String title,

        String description,

        @DateTimeFormat(pattern = "yyyy-MM-dd") // För <input type="date">
        LocalDate releaseDate,

        @NotNull(message = "There must be people working on the project.")
        Set<Long> employeesId,

        @NotNull(message = "Choose a category!")
        Category category,

        @NotNull(message = "Choose a genre!")
        Genre genre,

        @NotNull(message = "What company does the project belong to")
        Long companyId,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // För <input type="datetime-local">
        @NotNull(message = "Choose a deadline")
        LocalDateTime recruitingDeadline,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull(message = "Choose a deadline")
        LocalDateTime recordingDeadline,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull(message = "Choose a deadline")
        LocalDateTime editingDeadline
) {}



