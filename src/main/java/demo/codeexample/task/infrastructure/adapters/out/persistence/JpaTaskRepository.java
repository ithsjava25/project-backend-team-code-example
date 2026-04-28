package demo.codeexample.task.infrastructure.adapters.out.persistence;

import demo.codeexample.task.domain.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTaskRepository extends JpaRepository<TaskEntity,Long> {
    List<TaskEntity> findByProjectId(Long projectId);

    Optional<TaskEntity> findByProjectIdAndTaskType(Long projectId, TaskType taskType);
}
