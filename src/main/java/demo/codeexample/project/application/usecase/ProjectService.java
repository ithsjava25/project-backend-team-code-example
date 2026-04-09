package demo.codeexample.project.application.usecase;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort){
        this.repository = repository;
        this.userPort = userPort;
    }

    @Override
    public List<Project> findAllProjects() {
        return repository.findAll();
    }

    @Override
    public List<Project> findProjectByCategory(Category category) {
        return repository.findProjectByCategory(category);
    }

    @Override
    public List<Project> findProjectByGenre(Genre genre) {
        return repository.findProjectByGenre(genre);
    }

    @Override
    public Project createProject(String title, String description, LocalDate releaseDate, Long producerId,
                                 Category category, Genre genre, String imageURL) {
        Project newProject = Project.createNew(title, description, releaseDate, producerId,
                category, genre, imageURL);
        return repository.save(newProject);
    }

    @Override
    public Optional<Project> findProjectByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public ProjectDetails getProjectDetails(Long projectId) {
        return null;
    }
}
