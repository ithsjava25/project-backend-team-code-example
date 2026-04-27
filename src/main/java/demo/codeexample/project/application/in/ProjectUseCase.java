package demo.codeexample.project.application.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.ProjectLookup;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectUseCase extends ProjectLookup {

    boolean isDeadlinesInOrder(LocalDateTime recruitingDeadline, LocalDateTime recordingDeadline, LocalDateTime editingDeadline);

    List<ProjectDto> findAllCompletedProjectsByCompany(String companyName);

    List<ProjectDto> findAllNotCompleteProjectsByCompany(String companyName);

    List<ProjectDto> findProjectByGenre(Genre genre);

    List<ProjectDto> findProjectContainingTitle(String title);

    Optional<ProjectDto> findProjectByTitle(String title);

    Project createProject(CreateProjectDto projectDto);

    ProjectDto getProjectDetails(Long projectId);

    void finalizeProject(Long projectId);

    List<ProjectDto> findProjectsForUser(Long userId, String companyName);

    List<ProjectDto> findCurrentProjectsForUser(Long userId, String companyName);

    List<ProjectDto> findCompletedProjectsForUser(Long userId, String companyName);
}
