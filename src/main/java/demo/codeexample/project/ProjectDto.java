package demo.codeexample.project;

import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDto {
    Long id;
    String title;
    String description;
    LocalDate releaseDate;
    Long producerId;
    Category category;
    Genre genre;
    String imageUrl;
}
