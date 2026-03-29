package demo.codeexample.project;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long id;

    @NotNull
    String title;

    LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    Category category;

    @Enumerated(EnumType.STRING)
    Genre genre;

    String imageURL;

    public ProjectEntity(String title, LocalDate releaseDate, Category category, Genre genre, String imageURL){
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.genre = genre;
        this.imageURL = imageURL;
    }

}
