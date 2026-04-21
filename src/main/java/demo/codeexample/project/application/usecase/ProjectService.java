package demo.codeexample.project.application.usecase;

import demo.codeexample.project.ProjectDto;
import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;
    private final ProjectEventPort projectEventPort;
    private final ModelMapper mapper;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort, ProjectEventPort projectEventPort, ModelMapper mapper) {
        this.repository = repository;
        this.userPort = userPort;
        this.projectEventPort = projectEventPort;
        this.mapper = mapper;
    }

    @Override
    public List<ProjectDto> findAllProjects() {
        return repository.findAllByOrderByTitleAsc().stream()
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    public List<ProjectDto> findProjectByCategory(Category category) {
        return repository.findProjectByCategory(category).stream()
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    public List<Project> findProjectByGenre(Genre genre) {
        return repository.findProjectByGenre(genre);
    }

    @Override
    public Project createProject(String title, String description, LocalDate releaseDate, Set<Long> employeesId,
                                 Category category, Genre genre, Long companyId,
                                 LocalDateTime recruitingDeadline,
                                 LocalDateTime recordingDeadline,
                                 LocalDateTime editingDeadline) {

        userPort.validateEmployees(employeesId);

        Project newProject = Project.builder()
                .id(null)
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .employeesId(employeesId)
                .category(category)
                .genre(genre)
                .companyId(companyId)
                .build();
        Project savedProject = repository.save(newProject);

        ProjectCreatedEvent event = new ProjectCreatedEvent(
                savedProject.getId(),
                savedProject.getTitle(),
                savedProject.getEmployeesId(),
                savedProject.getReleaseDate(),
                savedProject.getCompanyId(),
                recruitingDeadline,
                recordingDeadline,
                editingDeadline );

        projectEventPort.publish(event);

        return savedProject;
    }

    @Override
    public List<Project> findProjectContainingTitle(String title) {
        return repository.findProjectContainingTitle(title);
    }

    public ProjectDto getProjectDetails(Long projectId) {
        return repository.findById(projectId)
                .map(project -> mapper.map(project, ProjectDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
    }

}