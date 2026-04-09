package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByCategory(Category category);

    List<ProjectEntity> findByGenre(Genre genre);

    List<ProjectEntity> findByTitleContainingIgnoreCase(String title);
}
