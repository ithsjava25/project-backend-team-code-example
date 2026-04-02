package demo.codeexample.task;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskDto {

    @NotBlank
    private String title ;

    @NotBlank
    private String description ;

    @Future
    private LocalDateTime deadline ;

    @NotNull
    private Long projectId ;

    @NotNull
    private Long userId ;

}
