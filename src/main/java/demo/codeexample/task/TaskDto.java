package demo.codeexample.task;

import demo.codeexample.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private long id;

    private String title ;

    private String description ;

    private TaskStatus status ;

    private LocalDateTime deadline ;

    private Long projectId ;

    private Long userId ;

}
