package demo.codeexample.project;

import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDto {
        Long id;
        String title;
        String description;
        LocalDate releaseDate;
        Category category;
        Genre genre;
        Long fileId;

        @NotNull
        Long companyId;
}
