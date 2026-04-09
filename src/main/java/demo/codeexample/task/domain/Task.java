package demo.codeexample.task.domain;

import demo.codeexample.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Task {

    @Setter(AccessLevel.NONE)
    private final Long taskId ;
    private final String title ;
    private final String description ;
    private final TaskStatus status ;
    private final LocalDateTime deadline ;
    private final Long projectId ;
    private final Long userId ;


    public Task(Long taskId, String title, String description, TaskStatus status, LocalDateTime deadline, Long projectId, Long userId) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.projectId = projectId;
        this.userId = userId;
    }


    public static Task createNew(String title, String description, TaskStatus status, LocalDateTime deadline, Long projectId, Long userId) {
        return new Task(null, title, description, status, deadline, projectId, userId);
    }

}
