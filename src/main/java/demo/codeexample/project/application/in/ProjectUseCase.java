package demo.codeexample.project.application.in;

import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectUseCase {

    List<Project> findAllProjects();

    List<Project> findProjectByCategory(Category category);

    List<Project> findProjectByGenre(Genre genre);

    List<Project> findProjectContainingTitle(String title);

    Project createProject(String title, String description, LocalDate releaseDate, Long producerId,
                          Category category, Genre genre, String imageURL);

    ProjectDetails getProjectDetails(Long projectId);

    record ProjectDetails(Long projectId, String title, String description, LocalDate releaseDate,
                          Category category, Genre genre, String imageUrl){}
}
