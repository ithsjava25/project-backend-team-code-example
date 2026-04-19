package demo.codeexample.project.application.usecase;

import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.domain.Project;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;
    private final ModelMapper mapper;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort, ModelMapper mapper) {
        this.repository = repository;
        this.userPort = userPort;
        this.mapper = mapper;
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
    @Transactional
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
                editingDeadline

        );
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