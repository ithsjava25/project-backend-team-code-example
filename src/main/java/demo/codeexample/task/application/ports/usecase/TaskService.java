package demo.codeexample.task.application.ports.usecase;

import demo.codeexample.shared.LoggerAction;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final LoggerLookup logger;



    public TaskService(TaskRepositoryPort taskRepository, LoggerLookup logger) {
        this.taskRepository = taskRepository;
        this.logger = logger;
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
    public Task createTask(String title,
                String description,
                TaskStatus status,
                LocalDateTime deadline,
                Long projectId,
                Long userId){
        Task task = Task.createNew  (title, description, status, deadline, projectId, userId);

        Task savedTask = taskRepository.save(task);
        logger.log(LoggerAction.TASK_CREATED, userId, "TASK", savedTask.getId());
        return savedTask;

    }
}
