package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.usecase.ProjectService;
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
    public List<Project> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Project> findAllBelongingToCompany(Long companyId) {
        return jpaRepository.findByCompanyId(companyId).stream()
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
    public Project save(CreateProjectDto project) {
        ProjectEntity entity = mapper.map(project, ProjectEntity.class);
        ProjectEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    private ProjectEntity toEntity(Project project){
        return new ProjectEntity(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getReleaseDate(),
                project.getEmployeesId(),
                project.getCategory(),
                project.getGenre(),
                project.getCompanyId()
        );
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