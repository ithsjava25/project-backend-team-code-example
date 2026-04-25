package demo.codeexample.task;

import demo.codeexample.comment.CommentDto;
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
    private Long id;
    private TaskType taskType;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private Long projectId;
    private Long userId;
    private String userName;
    private List<CommentDto> comments;
    private String projectName;
}

