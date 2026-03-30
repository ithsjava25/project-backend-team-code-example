package demo.codeexample.project;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import lombok.*;

import java.time.LocalDate;

@Data
public class ProjectDto {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    //private Set<TaskDto> taskList;
    private Long producerId;
    private Category category;
    private Genre genre;
    private String imageURL;
}
