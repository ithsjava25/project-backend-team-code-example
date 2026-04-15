package demo.codeexample.project.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Project {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;
    private final Set<Long> employeesId;
    private final Category category;
    private Genre genre;
    private Long fileId;  //<-- contains image(front cover), video (trailer), pdf
    private Long companyId;

}
