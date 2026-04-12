package demo.codeexample.project.application.usecase;

import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.exceptions.UserAuthorizationException;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;
import jakarta.persistence.EntityNotFoundException;

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
    public ProjectDto getProjectDetails(Long projectId) {
        return repository.findById(projectId)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
    }

    private ProjectDto mapToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setName(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setReleaseDate(project.getReleaseDate());
        dto.setCategory(project.getCategory());
        dto.setGenre(project.getGenre());
        dto.setImageUrl(project.getImageURL());
        return dto;
    }

}

