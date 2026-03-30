package demo.codeexample.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentDto {

    @NotBlank(message = "Comment can't be empty.")
    @Size(max = 300, message = "Can't be more than 300 characters.")
    private String content;

}
