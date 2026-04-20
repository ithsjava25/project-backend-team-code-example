package demo.codeexample.task.infrastructure.adapters.out.persistence;

import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskPersistenceAdapter implements TaskRepositoryPort {

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
                task.getTaskType(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                task.getProjectId(),
                task.getUserId()
        );
    }



}
