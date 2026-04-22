package demo.codeexample.project;

import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record CreateProjectDto(
        @NotBlank(message = "Project must have a title.")
        String title,

        String description,
        LocalDate releaseDate,

        @NotNull(message = "There must be people working on the project.")
        Set<Long> employeesId,

        @NotBlank(message = "Choose a category!")
        Category category,

        @NotBlank(message = "Choose a genre!")
        Genre genre,

        @NotNull(message = "What company created the project?")
        Long companyId
) {}



