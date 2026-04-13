package demo.codeexample.comment.application;
import demo.codeexample.comment.CommentDto;
import demo.codeexample.comment.CommentFacade;
import demo.codeexample.comment.CommentLookup;
import demo.codeexample.comment.domain.CommentRepository;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.comment.domain.Comment;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService implements CommentLookup {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    //private final "Some kind of Authorization service" with saved user

    //Add
    public CommentDto createComment(CreateCommentDto createCommentDto) {

        // Long creatorId = Any AuthService

        Comment commentEntity = modelMapper.map(createCommentDto, Comment.class);
        // commentEntity.setCreatorId(creatorId)
        commentRepository.save(commentEntity);

        return modelMapper.map(commentEntity, CommentDto.class);
    }

    @Override // Implementing the method from your Facade
    public List<CommentDto> getCommentsForTask(long taskId) {
        // Use a repository method to find comments by Task ID
        return commentRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId).stream()
                .map(entity -> modelMapper.map(entity, CommentDto.class))
                .toList();
    }

    @Override // Implementing the method from your Facade
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
