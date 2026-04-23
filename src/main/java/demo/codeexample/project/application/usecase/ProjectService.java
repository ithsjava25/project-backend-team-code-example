package demo.codeexample.project.application.usecase;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.out.ProjectEventPort;
import demo.codeexample.shared.Category;
import demo.codeexample.project.application.out.CompanyPort;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;
import demo.codeexample.shared.Role;
import demo.codeexample.user.UserLookup;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;
    private final CompanyPort companyPort;
    private final ProjectEventPort projectEventPort;
    private final ModelMapper mapper;
    private final UserLookup userLookup;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort,
                          ProjectEventPort projectEventPort, CompanyPort companyPort, ModelMapper mapper, UserLookup userLookup) {
        this.repository = repository;
        this.userPort = userPort;
        this.projectEventPort = projectEventPort;
        this.companyPort = companyPort;
        this.mapper = mapper;
        this.userLookup = userLookup;
    }

    @Override
    public List<ProjectDto> findAllProjects() {
        return repository.findAllByOrderByTitleAsc().stream()
                .map(entity -> mapper.map(entity, ProjectDto.class))
                .toList();
    }

    @Override
    public List<ProjectDto> findAllProjectsFromCompany(String companyName) {
        Long companyId = companyPort.getCompanyIdFromName(companyName);
        return repository.findAllBelongingToCompany(companyId).stream()
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

        Project project = repository.save(projectDto);

        ProjectCreatedEvent event = new ProjectCreatedEvent(
                project.getId(),
                project.getTitle(),
                project.getEmployeesId(),
                project.getReleaseDate(),
                project.getCompanyId(),
                projectDto.recruitingDeadline(),
                projectDto.recordingDeadline(),
                projectDto.editingDeadline()
        );
        projectEventPort.publish(event);

        return project;
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


    @Override
    public List<ProjectDto> findProjectsForUser(Long userId) {
        var user = userLookup.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.PRODUCER) {
            return findAllProjects();
        }

        return repository.findAll().stream()
                .filter(project -> project.getEmployeesId() != null
                        && project.getEmployeesId().contains(userId))
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }
}