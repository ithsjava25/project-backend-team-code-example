package demo.codeexample.project.infrastructure.adapters.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByTitle(String title);
}
