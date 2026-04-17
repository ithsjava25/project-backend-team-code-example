package demo.codeexample.project.application.out;

import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {

    List<ProjectDto> findAll();

    List<ProjectDto> findProjectByCategory(Category category);

    List<ProjectDto> findProjectByGenre(Genre genre);

    List<ProjectDto> findProjectContainingTitle(String title);

    ProjectDto save(Project project);

    Optional<ProjectDto> findById(Long id);
}
