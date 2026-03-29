package demo.codeexample.project;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {
    @NotNull(message = "You must choose a title for the project")
    String title;

    LocalDate releaseDate;

    @NotNull(message = "You must choose a category")
    Category category;

    @NotNull(message = "You must choose a genre")
    Genre genre;

    String imageURL;
}
