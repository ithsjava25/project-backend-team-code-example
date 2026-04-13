package demo.codeexample.task.infrastructure.adapters.in;
import demo.codeexample.comment.CommentLookup;
import demo.codeexample.task.TaskResponseDto;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.domain.Task;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;



@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskUseCase taskUseCase;
    private final CommentLookup commentLookup;
    private final ModelMapper modelMapper;


    @GetMapping("/{taskId}/view")
    public String viewTask(@PathVariable Long taskId, Model model) {
        // 1. Get Domain Entity
        Task task = taskUseCase.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 2. Map core fields (ModelMapper matches names automatically)
        TaskResponseDto dto = modelMapper.map(task, TaskResponseDto.class);

        // 3. Manually attach the comments from the other module
        dto.setComments(commentLookup.getCommentsForTask(taskId));

        model.addAttribute("task", dto);
        return "task-detail";
    }
}
