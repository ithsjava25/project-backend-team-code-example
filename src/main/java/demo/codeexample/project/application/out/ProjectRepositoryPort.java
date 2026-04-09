package demo.codeexample.project.application.out;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {

    List<Project> findAll();

    List<Project> findProjectByCategory(Category category);

    List<Project> findProjectByGenre(Genre genre);

    List<Project> findProjectContainingTitle(String title);

    Project save(Project project);

    Optional<Project> findById(Long id);
}
