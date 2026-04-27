package demo.codeexample.task.infrastructure.adapters.out.persistence;

import demo.codeexample.project.TaskLookup;
import demo.codeexample.task.TaskResponseDto;
import demo.codeexample.task.TaskSummaryDto;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.task.domain.TaskType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskPersistenceAdapter implements TaskRepositoryPort, TaskLookup {

    private final JpaTaskRepository jpaTaskRepository;
    private final ModelMapper mapper;

    public TaskPersistenceAdapter(JpaTaskRepository jpaTaskRepository, ModelMapper mapper) {
    this.jpaTaskRepository = jpaTaskRepository;
        this.mapper = mapper;
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
                .map(this::toDomain);
    }

    @Override
    public List<TaskSummaryDto> getTasksByProjectId(Long projectId) {
        return jpaTaskRepository.findByProjectId(projectId)
                .stream()
                .map(entity -> new TaskSummaryDto(
                        entity.getId(),
                        entity.getTaskType(),
                        entity.getStatus(),
                        entity.getProjectId()
                ))
                .toList();
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
        return jpaTaskRepository.findByProjectIdAndTaskType(projectId, TaskType.EDITING)
                .map(entity -> entity.getStatus() == TaskStatus.COMPLETED)
                .orElse(false);
    }
}
