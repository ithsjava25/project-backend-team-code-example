package demo.codeexample.project;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class CreateProjectDto {
    @NotNull(message = "You must choose a title for the project")
    String title;

    @Future(message = "Movie can't be released in past")
    LocalDate releaseDate;

    @NotNull(message = "You must choose a category")
    Category category;

    @NotNull(message = "You must choose a genre")
    Genre genre;

    String imageURL;
}
