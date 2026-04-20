package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.shared.Category;
import demo.codeexample.project.domain.Genre;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.domain.Project;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
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
    public List<Project> findAllByOrderByTitleAsc() {
        return jpaRepository.findAll(Sort.by("title").ascending())
                .stream()
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
    public List<Project> findProjectContainingTitle(String title) {
        return jpaRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity saved = jpaRepository.save(entity);
        jpaRepository.flush();
        return toDomain(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    private ProjectEntity toEntity(Project project){
        return mapper.map(project, ProjectEntity.class);
    }

    private Project toDomain(ProjectEntity entity){
        return Project.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .releaseDate(entity.getReleaseDate())
                .employeesId(entity.getEmployeesId())
                .category(entity.getCategory())
                .genre(entity.getGenre())
                .companyId(entity.getCompanyId())
                .build();
    }
}