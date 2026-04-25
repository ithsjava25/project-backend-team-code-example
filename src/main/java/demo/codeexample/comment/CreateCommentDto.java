package demo.codeexample.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {
    private Long taskId;
    private Long writerId;

    @NotBlank(message = "Comment can't be empty.")
    @Size(max = 300, message = "Can't be more than 300 characters.")
    private String content;
}
