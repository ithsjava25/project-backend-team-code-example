package demo.codeexample.project;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;

@Data
public class CreateProjectDto {
    @NotBlank(message = "Choose a title for the project!")
    private String title;

    @Future(message = "Movie can't be released in past")
    private LocalDate releaseDate;

    //private List<CreateTaskDto> tasks;

    @NotBlank(message = "Who is the producer?")
    private Long producerId;

    @NotNull(message = "You must choose a category!")
    Category category;

    @NotNull(message = "You must choose a genre!")
    Genre genre;

    String imageURL;
}
