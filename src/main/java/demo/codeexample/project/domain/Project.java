package demo.codeexample.project.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;

    @Setter(AccessLevel.NONE)
    private Set<Long> employeesId;

    @Setter(AccessLevel.NONE)
    private Category category;

    private Genre genre;
    private Long companyId;

}
