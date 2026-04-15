package demo.codeexample.task.application.ports.in;

import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Porten för allt som kommer in kan göra

public interface TaskUseCase {

    List<Task> findAll();
    Optional<Task> findById(Long id);

    Task createTask(TaskType taskType, String description, TaskStatus status,
                    LocalDateTime deadline, Long projectId, Long userId);

}
