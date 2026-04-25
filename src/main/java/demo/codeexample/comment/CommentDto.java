package demo.codeexample.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    private String content;

    private Long userId;

    private LocalDateTime createdAt;


}
