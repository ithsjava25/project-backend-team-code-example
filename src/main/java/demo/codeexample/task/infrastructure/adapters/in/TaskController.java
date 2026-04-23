package demo.codeexample.task.infrastructure.adapters.in;
import demo.codeexample.comment.CommentLookup;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.task.TaskResponseDto;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.domain.Task;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;



@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskUseCase taskUseCase;
    private final CommentLookup commentLookup;
    private final ModelMapper modelMapper;


    @GetMapping("/{taskId}/view")
    public String viewTask(@PathVariable Long taskId, Model model) {
        Task task = taskUseCase.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TaskResponseDto dto = modelMapper.map(task, TaskResponseDto.class);

        dto.setComments(commentLookup.getCommentsForTask(taskId));

        model.addAttribute("task", dto);
        return "task/taskPage";
    }

    @PostMapping("hexagonal/{taskId}/comments")
    public String addComment(@PathVariable Long taskId, @Valid CreateCommentDto dto) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();

        Long userId = principal.equals("anonymousUser") ? 1L : Long.parseLong(principal);

        taskUseCase.addComment(taskId, dto.getContent(), userId);
        return "redirect:/" + taskId + "/view";
}
