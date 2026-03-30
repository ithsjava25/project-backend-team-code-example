package demo.codeexample.project;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;
    private final ModelMapper modelMapper;

    public List<ProjectDto> getAllProjects(){
        return repository.findAll().stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .toList();
    }

    public Optional<ProjectDto> getProjectFromTitle(String title){
        return repository.findByTitle(title)
                .map(project -> modelMapper.map(project, ProjectDto.class));
    }



}
