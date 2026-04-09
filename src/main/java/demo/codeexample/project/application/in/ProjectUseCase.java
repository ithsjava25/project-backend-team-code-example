package demo.codeexample.project.application.in;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.domain.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectUseCase {

    List<Project> findAllProjects();

    List<Project> findProjectByCategory(Category category);

    List<Project> findProjectByGenre(Genre genre);

    Project createProject(String title, String description, LocalDate releaseDate, Long producerId,
                          Category category, Genre genre, String imageURL);

    Optional<Project> findProjectByTitle(String title);

    ProjectDetails getProjectDetails(Long projectId);

    record ProjectDetails(Long projectId, String title, String description, LocalDate releaseDate,
                          Category category, Genre genre, String imageUrl){}
}
