package demo.codeexample.project.application.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.ProjectLookup;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectUseCase extends ProjectLookup {

    List<ProjectDto> findAllCompletedProjectsByCompany(String companyName);

    List<ProjectDto> findAllNotCompleteProjectsByCompany(String companyName);

    List<ProjectDto> findProjectByGenre(Genre genre);

    List<ProjectDto> findProjectContainingTitle(String title);

    Optional<ProjectDto> findProjectByTitle(String title);

    Project createProject(CreateProjectDto projectDto);

    ProjectDto getProjectDetails(Long projectId);

    List<ProjectDto> findProjectsForUser(Long userId);

    List<ProjectDto> findCurrentProjectsForUser(Long userId, String companyName);

    List<ProjectDto> findCompletedProjectsForUser(Long userId, String companyName);
}
