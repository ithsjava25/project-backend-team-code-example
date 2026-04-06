package demo.codeexample.task;

import demo.codeexample.project.infrastructure.adapters.out.ProjectEntity;
import demo.codeexample.project.infrastructure.adapters.out.JpaProjectRepository;
import demo.codeexample.user.UserEntity;
import demo.codeexample.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final JpaProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskDto createTask(CreateTaskDto dto) {

        ProjectEntity project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskEntity task = modelMapper.map(dto, TaskEntity.class);

        task.setProject(project);
        task.setUser(user);

        TaskEntity saved = taskRepository.save(task);

        return modelMapper.map(saved, TaskDto.class);
    }
}
