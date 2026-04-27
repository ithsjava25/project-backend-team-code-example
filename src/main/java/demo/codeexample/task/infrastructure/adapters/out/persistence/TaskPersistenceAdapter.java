package demo.codeexample.task.infrastructure.adapters.out.persistence;

import demo.codeexample.project.TaskLookup;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.task.domain.TaskType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskPersistenceAdapter implements TaskRepositoryPort, TaskLookup {

    private final JpaTaskRepository jpaTaskRepository;

    public TaskPersistenceAdapter(JpaTaskRepository jpaTaskRepository) {
    this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public List<Task> findAll() {
        return jpaTaskRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = toEntity(task);
        TaskEntity saved = jpaTaskRepository.save(entity);
        jpaTaskRepository.flush();
        return toDomain(saved);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaTaskRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Task> findByProjectIdAndTaskType(Long projectId, TaskType taskType) {
        return jpaTaskRepository.findByProjectIdAndTaskType(projectId, taskType)
                .map(this::toDomain); // Map the entity back to your Domain Task
    }


    private Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getTaskType(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getDeadline(),
                entity.getProjectId(),
                entity.getUserId()
        );
    }

    private TaskEntity toEntity(Task task) {
        return new TaskEntity(
                task.getId(),
                task.getTaskType(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                task.getProjectId(),
                task.getUserId()
        );
    }


    @Override
    public boolean isFinalTaskComplete(Long projectId) {
        // We look for the last stage (EDITING) and check its status
        return jpaTaskRepository.findByProjectIdAndTaskType(projectId, TaskType.EDITING)
                .map(entity -> entity.getStatus() == TaskStatus.COMPLETED)
                .orElse(false);
    }
}
