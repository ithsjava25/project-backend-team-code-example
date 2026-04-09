package demo.codeexample.project.application.usecase;

import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.exceptions.UserAuthorizationException;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;

import java.time.LocalDate;
import java.util.List;

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
        if(!userPort.validateProducer(producerId))
            throw new UserAuthorizationException(producerId);

        Project newProject = Project.createNew(title, description, releaseDate, producerId,
                category, genre, imageURL);
        return repository.save(newProject);
    }

    @Override
    public List<Project> findProjectContainingTitle(String title) {
        return repository.findProjectContainingTitle(title);
    }

    @Override
    public ProjectDetails getProjectDetails(Long projectId) {
        return null;
    }

}
