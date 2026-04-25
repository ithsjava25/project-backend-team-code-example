package demo.codeexample.comment.application;
import demo.codeexample.comment.CommentDto;
import demo.codeexample.comment.CommentLookup;
import demo.codeexample.comment.domain.CommentRepository;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.comment.domain.Comment;
import demo.codeexample.security.UserAuthHelper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService implements CommentLookup {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserAuthHelper userAuthHelper;

    //Add
    public CommentDto createComment(CreateCommentDto dto) {
        Long writedId = userAuthHelper.getCurrentUserId();

        Comment commentEntity = new Comment();
        commentEntity.setContent(dto.getContent());
        commentEntity.setTaskId(dto.getTaskId());
        commentEntity.setUserId(writedId);

        commentRepository.save(commentEntity);

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
