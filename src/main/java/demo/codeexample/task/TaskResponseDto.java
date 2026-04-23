package demo.codeexample.task;

import demo.codeexample.comment.CommentDto;
import demo.codeexample.task.domain.Task;
import demo.codeexample.task.domain.TaskStatus;
import demo.codeexample.task.domain.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long Id;
    private TaskType taskType; // Add this!
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private Long projectId;
    private Long userId;
    private List<CommentDto> comments;
    private String projectName;
}

