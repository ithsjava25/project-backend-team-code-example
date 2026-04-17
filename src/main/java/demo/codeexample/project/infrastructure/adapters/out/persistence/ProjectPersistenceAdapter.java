package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
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
    public List<ProjectDto> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ProjectDto> findProjectByCategory(Category category) {
        return jpaRepository.findByCategory(category).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ProjectDto> findProjectByGenre(Genre genre) {
        return jpaRepository.findByGenre(genre).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ProjectDto> findProjectContainingTitle(String title) {
        return jpaRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public ProjectDto save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ProjectDto> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    private ProjectEntity toEntity(Project project){
        return mapper.map(project, ProjectEntity.class);
    }

    private ProjectDto toDomain(ProjectEntity entity){
        return mapper.map(entity, ProjectDto.class);
    }
}