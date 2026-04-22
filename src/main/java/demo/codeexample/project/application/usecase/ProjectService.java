package demo.codeexample.project.application.usecase;

import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.out.ProjectEventPort;
import demo.codeexample.project.application.out.SecurityPort;
import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;
import demo.codeexample.shared.LoggerAction;
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
    private final SecurityPort securityPort;
    private final ModelMapper mapper;
    private final LoggerLookup logger;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort, ProjectEventPort projectEventPort, SecurityPort securityPort, ModelMapper mapper, LoggerLookup logger) {
        this.repository = repository;
        this.userPort = userPort;
        this.projectEventPort = projectEventPort;
        this.securityPort = securityPort;
        this.mapper = mapper;
        this.logger = logger;
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
        Long currentUserId = securityPort.getCurrentUserId();
        String creatorName = securityPort.getCurrentUserName();

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

        logger.log(
                LoggerAction.PROJECT_CREATED,
                //currentUserId, ---- Funkar just nu inte och kan inte testa förrens vi fixat login page.
                1L,
                "PROJECT",
                savedProject.getId(),
                savedProject.getId(),
                "New project created: " + savedProject.getTitle() + ". Created by: " + creatorName
        );

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