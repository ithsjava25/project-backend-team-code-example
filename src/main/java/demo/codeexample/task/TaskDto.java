package demo.codeexample.task;

import demo.codeexample.task.domain.TaskStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private Long projectId;
    private Long userId;
}
