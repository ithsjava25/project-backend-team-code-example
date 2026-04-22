package demo.codeexample.project.application.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ProjectUseCase {

    List<ProjectDto> findAllProjects();

    List<ProjectDto> findAllProjectsFromCompany(String companyName);

    List<ProjectDto> findProjectByCategory(Category category);

    List<ProjectDto> findProjectByGenre(Genre genre);

    List<ProjectDto> findProjectContainingTitle(String title);

    Project createProject(CreateProjectDto newProject);

    ProjectDto getProjectDetails(Long projectId);
}
