package demo.codeexample.project;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;

    public List<ProjectEntity> getAllProjects(){return repository.findAll();}

    public Optional<ProjectEntity> getProjectFromTitle(String title){
        return repository.findByTitle(title);
    }

}
