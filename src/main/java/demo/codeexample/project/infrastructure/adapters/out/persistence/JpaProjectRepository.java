package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByCategory(Category category);

    List<ProjectEntity> findByGenre(Genre genre);
}
