package demo.codeexample.project;

import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
public class ProjectDto {
        Long id;
        String title;
        String description;
        LocalDate releaseDate;
        Category category;
        Genre genre;
        String companyName;
        boolean completed;
}
