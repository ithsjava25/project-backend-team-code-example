package demo.codeexample.project;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import lombok.*;

import java.time.LocalDate;

@Data
public class ProjectDto {
    Long id;
    String title;
    LocalDate releaseDate;
    User producer;
    Category category;
    Genre genre;
    String imageURL;
}
