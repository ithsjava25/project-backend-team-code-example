package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.domain.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {

    private JpaProjectRepository jpaRepository;
    private ModelMapper mapper;

    public ProjectPersistenceAdapter(JpaProjectRepository jpaRepository, ModelMapper mapper){
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Project> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Project> findProjectByCategory(Category category) {
        return jpaRepository.findByCategory(category).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Project> findProjectByGenre(Genre genre) {
        return jpaRepository.findByGenre(genre).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        Optional<ProjectEntity> entity = jpaRepository.findById(id);
        return Optional.of(mapper.map(entity, Project.class));
    }

    private ProjectEntity toEntity(Project project){
        return mapper.map(project, ProjectEntity.class);
    }

    private Project toDomain(ProjectEntity entity){
        return mapper.map(entity, Project.class);
    }


}
