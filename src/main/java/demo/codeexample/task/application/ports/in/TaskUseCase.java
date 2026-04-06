package demo.codeexample.task.application.ports.in;

import demo.codeexample.enums.TaskStatus;
import demo.codeexample.task.domain.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Porten för allt som kommer in kan göra

public interface TaskUseCase {

    List<Task> findAll();
    Optional<Task> findById(Long id);

    Task createTask(Long id,
                    String title,
                      String description,
                      TaskStatus status,
                      LocalDateTime deadline,
                    Long projectId,
                      Long userId);

}
