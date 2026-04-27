package demo.codeexample.comment.application;
import demo.codeexample.comment.CommentDto;
import demo.codeexample.comment.CommentLookup;
import demo.codeexample.comment.domain.CommentRepository;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.comment.domain.Comment;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.project.TaskLookup;
import demo.codeexample.security.UserAuthHelper;
import demo.codeexample.shared.LoggerAction;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService implements CommentLookup {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserAuthHelper userAuthHelper;
    private final LoggerLookup logger;

    //Add
    public CommentDto createComment(CreateCommentDto dto) {
        Long writerId = userAuthHelper.getCurrentUserId();
        String userName = userAuthHelper.getCurrentUserName();
        if (writerId == null) {
            throw new AccessDeniedException("Authentication Required");
        }

        Comment commentEntity = new Comment();
        commentEntity.setContent(dto.getContent());
        commentEntity.setTaskId(dto.getTaskId());
        commentEntity.setUserId(writerId);
        commentEntity.setUserName(userName);

        Comment savedComment =commentRepository.save(commentEntity);
        logger.log(
                LoggerAction.COMMENT_ADDED,
                writerId,
                "TASK",
                savedComment.getId(),
                dto.getTaskId(),
                "User " + userName + " added a comment to task " + dto.getTaskId()
        );

        return modelMapper.map(commentEntity, CommentDto.class);
    }

    @Override // Implementing the method from your Facade
    public List<CommentDto> getCommentsForTask(long taskId) {

        return commentRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId).stream()
                .map(entity -> modelMapper.map(entity, CommentDto.class))
                .toList();
    }

    @Override
    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getAllComments() {
        List<Comment> commentEntities = commentRepository.findAll();
        final List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment commentEntity : commentEntities) {
            commentDtos.add(modelMapper.map(commentEntity, CommentDto.class));
        }
        return commentDtos;
    }
}
