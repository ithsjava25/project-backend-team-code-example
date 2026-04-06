package demo.codeexample.project.infrastructure.adapters.out;

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

    private JpaProjectRepository projectRepository;
    private ModelMapper mapper;

    public ProjectPersistenceAdapter(JpaProjectRepository projectRepository, ModelMapper mapper){
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll().stream()
                .map(entity -> mapper.map(entity, Project.class))
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


}
