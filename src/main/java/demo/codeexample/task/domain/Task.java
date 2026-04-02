package demo.codeexample.task.domain;

import demo.codeexample.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

public class Task {

    private final Long id ;
    private final String title ;
    private final String description ;
    private final TaskStatus status ;
    private final LocalDateTime deadline ;
    private final Long projectId ;
    private final Long userId ;


    public Task(Long id, String title, String description, TaskStatus status, LocalDateTime deadline, Long projectId, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.projectId = projectId;
        this.userId = userId;
    }


    public static Task createNew(Long id, String title, String description, TaskStatus status, LocalDateTime deadline, Long projectId, Long userId) {
        return new Task(id, title, description, status, deadline, projectId, userId);
    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Long getProjectId() {
        return projectId;
    }
}
