package demo.codeexample.project.application.in;

import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.ProjectLookup;
import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ProjectUseCase extends ProjectLookup {

    List<ProjectDto> findAllProjects();

    List<ProjectDto> findProjectByCategory(Category category);

    List<Project> findProjectByGenre(Genre genre);

    List<Project> findProjectContainingTitle(String title);

    Project createProject(String title, String description, LocalDate releaseDate, Set<Long> employeesId,
                          Category category, Genre genre, Long companyId);

    ProjectDto getProjectDetails(Long projectId);
}
