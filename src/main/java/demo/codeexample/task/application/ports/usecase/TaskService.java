package demo.codeexample.task.application.ports.usecase;

import demo.codeexample.enums.TaskStatus;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskService implements TaskUseCase {


    public TaskService(TaskRepositoryPort taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(Long id) {
    return taskRepository.findById(id);
    }

    @Override
    public Task createTask(Long id, String title,
                String description,
                TaskStatus status,
                LocalDateTime deadline,
                Long projectId,
                Long userId){
        Task task = Task.createNew  (id, title, description, status, deadline, projectId, userId);
        return taskRepository.save(task);

    }
}
