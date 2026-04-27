package demo.codeexample.project.application.usecase;

import demo.codeexample.exceptions.DeadlineException;
import demo.codeexample.project.CreateProjectDto;
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
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;
    private final ProjectEventPort projectEventPort;
    private final SecurityPort securityPort;
    private final ModelMapper mapper;
    private final LoggerLookup logger;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort,
                          ProjectEventPort projectEventPort, SecurityPort securityPort, ModelMapper mapper, LoggerLookup logger) {
        this.repository = repository;
        this.userPort = userPort;
        this.projectEventPort = projectEventPort;
        this.securityPort = securityPort;
        this.mapper = mapper;
        this.logger = logger;
    }

    @Override
    public boolean isDeadlinesInOrder(LocalDateTime recruitingDeadline, LocalDateTime recordingDeadline, LocalDateTime editingDeadline) {
        if (recruitingDeadline == null || recordingDeadline == null || editingDeadline == null) {
            return true;
        }

        return recruitingDeadline.isBefore(recordingDeadline) && recordingDeadline.isBefore(editingDeadline);
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
        userPort.validateEmployees(projectDto.employeesId());

        if(isDeadlinesInOrder(projectDto.recruitingDeadline(), projectDto.recordingDeadline(), projectDto.editingDeadline())){
            Project project = repository.save(projectDto);

            Long currentUserId = securityPort.getCurrentUserId();
            String creatorName = securityPort.getCurrentUserName();

//        logger.log(
//                LoggerAction.PROJECT_CREATED,
//                currentUserId,
//                "PROJECT",
//                project.getId(),
//                project.getId(),
//                "New project created: " + project.getTitle() + ". Created by: " + creatorName
//        );

            ProjectCreatedEvent event = new ProjectCreatedEvent(
                    project.getId(),
                    project.getTitle(),
                    project.getEmployeesId(),
                    project.getReleaseDate(),
                    project.getCompanyName(),
                    projectDto.recruitingDeadline(),
                    projectDto.recordingDeadline(),
                    projectDto.editingDeadline()
            );
            projectEventPort.publish(event);

            return project;
        } else {
            throw new DeadlineException();

        }
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