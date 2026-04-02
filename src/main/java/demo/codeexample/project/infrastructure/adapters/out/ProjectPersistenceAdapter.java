package demo.codeexample.project.infrastructure.adapters.out;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.domain.Project;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private JpaProjectRepository projectRepository;

    public ProjectPersistenceAdapter(JpaProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Project> findProjectByCategory(Category category) {
        return List.of();
    }

    @Override
    public List<Project> findProjectByGenre(Genre genre) {
        return List.of();
    }

    @Override
    public Project save(Project project) {
        return null;
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.empty();
    }

    private Project toDomain(ProjectEntity entity){
        return new Project(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getReleaseDate(),
                entity.getProducerId(),
                entity.getCategory(),
                entity.getGenre(),
                entity.getImageURL()
        );
    }

    private ProjectEntity toEntity(Project project){

    }
}
