package demo.codeexample.project;

import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    private Set<Long> employeesId;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Genre is required")
    private Genre genre;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotNull
    private LocalDateTime recruitingDeadline;

    @NotNull
    private LocalDateTime recordingDeadline;

    @NotNull
    private LocalDateTime editingDeadline;
}



