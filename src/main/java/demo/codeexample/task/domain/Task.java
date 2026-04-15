package demo.codeexample.task.domain;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Task {

    @Setter(AccessLevel.NONE)
    private Long id ;
    private final TaskType taskType ;
    private String description ;
    private TaskStatus status ;
    private LocalDateTime deadline ;
    private final Long projectId ;
    private Long userId ;

}