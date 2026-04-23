package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.project.domain.Project;
import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByCategory(Category category);

    List<ProjectEntity> findByGenre(Genre genre);

    List<ProjectEntity> findByTitleContainingIgnoreCase(String title);

    List<ProjectEntity> findProjectsByCompanyNameAndCompletedOrderByReleaseDateDesc(String companyName, boolean completed);

    Optional<Project> findByTitle(String title);
}
