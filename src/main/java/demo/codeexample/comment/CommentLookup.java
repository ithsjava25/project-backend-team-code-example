package demo.codeexample.comment;

import java.util.List;

public interface CommentLookup {

    CommentDto createComment(CreateCommentDto dto);

    List<CommentDto> getCommentsForTask(long taskId);

    void deleteComment(long commentId);
}
