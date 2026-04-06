package demo.codeexample.task.application.ports.out;

import demo.codeexample.task.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    List<Task> findAll();

    Task save(Task task);

    Optional<Task> findById(Long id);
}
