package demo.codeexample.task.infrastructure.adapters.out.persistence;

import demo.codeexample.task.domain.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTaskRepository extends JpaRepository<TaskEntity,Long> {
    Optional<TaskEntity> findByProjectIdAndTaskType(Long projectId, TaskType taskType);
}
