package demo.codeexample.project.application.usecase;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.TaskLookup;
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
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;
    private final ProjectEventPort projectEventPort;
    private final SecurityPort securityPort;
    private final ModelMapper mapper;
    private final LoggerLookup logger;
    private final TaskLookup taskLookup;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort,
                          ProjectEventPort projectEventPort, SecurityPort securityPort, ModelMapper mapper, LoggerLookup logger, TaskLookup taskLookup) {
        this.repository = repository;
        this.userPort = userPort;
        this.projectEventPort = projectEventPort;
        this.securityPort = securityPort;
        this.mapper = mapper;
        this.logger = logger;
        this.taskLookup = taskLookup;
    }

    @Override
    public List<ProjectDto> findAllCompletedProjectsByCompany(String companyName) {
        return repository.findAllProjectsBelongingToCompany(companyName, true).stream()
                .map(entity -> mapper.map(entity, ProjectDto.class))
                .toList();
    }

    @Override
    public List<ProjectDto> findAllNotCompleteProjectsByCompany(String companyName) {
        return repository.findAllProjectsBelongingToCompany(companyName, false).stream()
                .map(entity -> mapper.map(entity, ProjectDto.class))
                .toList();
    }

    @Override
    public List<ProjectDto> findProjectByCategory(Category category) {
        return repository.findProjectByCategory(category).stream()
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    public List<ProjectDto> findProjectByGenre(Genre genre) {
        return repository.findProjectByGenre(genre).stream()
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    public Project createProject(CreateProjectDto projectDto) {
        if (!securityPort.hasRole("PRODUCER")) {
            throw new AccessDeniedException("Only a Producer can create projects.");
        }
        userPort.validateEmployees(projectDto.getEmployeesId());

        Project project = repository.save(projectDto);
        Long currentUserId = securityPort.getCurrentUserId();
        String creatorName = securityPort.getCurrentUserName();

        logger.log(
                LoggerAction.PROJECT_CREATED,
                currentUserId,
                "PROJECT",
                project.getId(),
                project.getId(),
                "New project created: " + project.getTitle() + ". Created by: " + creatorName
        );

        ProjectCreatedEvent event = new ProjectCreatedEvent(
                project.getId(),
                project.getTitle(),
                project.getEmployeesId(),
                project.getReleaseDate(),
                project.getCompanyName(),
                projectDto.getRecruitingDeadline(),
                projectDto.getRecordingDeadline(),
                projectDto.getEditingDeadline()
        );
        projectEventPort.publish(event);

        return project;
    }

    @Override
    public void finalizeProject(Long projectId) {
        if (!securityPort.hasRole("PRODUCER")) {
            throw new AccessDeniedException("Only a Producer can finalize projects.");
        }


        if (!taskLookup.isFinalTaskComplete(projectId)) {
            throw new IllegalStateException("Cannot finalize: The Editing task is not complete.");
        }

        Project project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        project.setCompleted(true);

        repository.save(project);

        String currentUserName = securityPort.getCurrentUserName();
        logger.log(
                LoggerAction.PROJECT_COMPLETED,
                securityPort.getCurrentUserId(),
                "PROJECT",
                projectId,
                projectId,
                "Project '" + project.getTitle() + "' marked as COMPLETED by " + currentUserName
        );
    }

    @Override
    public List<ProjectDto> findProjectContainingTitle(String title) {
        return repository.findProjectContainingTitle(title).stream()
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }

    @Override
    public Optional<ProjectDto> findProjectByTitle(String title) {
        return repository.findByTitle(title)
                .map(k -> mapper.map(k, ProjectDto.class));
    }

    public ProjectDto getProjectDetails(Long projectId) {
        return repository.findById(projectId)
                .map(project -> mapper.map(project, ProjectDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
    }

}