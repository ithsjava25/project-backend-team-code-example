package demo.codeexample.task;

import demo.codeexample.enums.TaskStatus;
import demo.codeexample.project.ProjectEntity;
import demo.codeexample.user.UserEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskDto {

    @NotBlank
    private String title ;

    @NotBlank
    private String description ;

    @NotBlank
    private TaskStatus status ;

    @Future
    private LocalDateTime deadline ;

    @NotBlank
    private Long projectId ;

    @NotBlank
    private Long userId ;

}
