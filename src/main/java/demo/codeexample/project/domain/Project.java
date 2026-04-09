package demo.codeexample.project.domain;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Project {

    @Setter(AccessLevel.NONE)
    private final Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;
    private final Long producerId;
    private final Category category;
    private Genre genre;
    private String imageURL;

    public Project(Long id, String title, String description, LocalDate releaseDate, Long producerId,
                   Category category, Genre genre, String imageURL) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.producerId = producerId;
        this.category = category;
        this.genre = genre;
        this.imageURL = imageURL;
    }

    public static Project createNew(String title, String description, LocalDate releaseDate, Long producerId,
                                    Category category, Genre genre, String imageURL){
        return new Project(null, title, description, releaseDate, producerId, category, genre, imageURL);
    }

}
