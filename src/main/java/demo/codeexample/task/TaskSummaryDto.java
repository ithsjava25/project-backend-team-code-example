package demo.codeexample.task;

import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.task.domain.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryDto {
    private Long id;
    private TaskType taskType;
    private TaskStatus status;
    private Long projectId;
}
