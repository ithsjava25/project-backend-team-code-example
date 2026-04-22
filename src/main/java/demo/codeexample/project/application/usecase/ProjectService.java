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
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort repository;
    private final UserPort userPort;
    private final CompanyPort companyPort;
    private final ProjectEventPort projectEventPort;
    private final ModelMapper mapper;

    public ProjectService(ProjectRepositoryPort repository, UserPort userPort,
                          ProjectEventPort projectEventPort, CompanyPort companyPort, ModelMapper mapper) {
        this.repository = repository;
        this.userPort = userPort;
        this.projectEventPort = projectEventPort;
        this.companyPort = companyPort;
        this.mapper = mapper;
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
    public Project createProject(CreateProjectDto newProject) {

        userPort.validateEmployees(newProject.employeesId());

        return repository.save(newProject);
    }

    @Override
    public List<ProjectDto> findProjectContainingTitle(String title) {
        return repository.findProjectContainingTitle(title).stream()
                .map(project -> mapper.map(project, ProjectDto.class))
                .toList();
    }

    public ProjectDto getProjectDetails(Long projectId) {
        return repository.findById(projectId)
                .map(project -> mapper.map(project, ProjectDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
    }

}