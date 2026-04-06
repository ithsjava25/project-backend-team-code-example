package demo.codeexample.project.application.usecase;

import demo.codeexample.enums.Category;
import demo.codeexample.enums.Genre;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.domain.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class ProjectService implements ProjectUseCase {

    ProjectRepositoryPort projectRepository;

    public ProjectService(ProjectRepositoryPort projectRepository){
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> findAllProjects() {
        return List.of();
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
    public Project createProject() {
        return null;
    }

    @Override
    public Optional<Project> findProjectByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public ProjectDetails getProjectDetails(Long projectId) {
        return null;
    }
}
