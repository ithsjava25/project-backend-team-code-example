package demo.codeexample.task.infrastructure.adapters.in;
import demo.codeexample.comment.CommentLookup;
import demo.codeexample.security.UserAuthHelper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;



@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskUseCase taskUseCase;
    private final CommentLookup commentLookup;
    private final UserAuthHelper userAuthHelper;
    private final ModelMapper modelMapper;


    @GetMapping("/{taskId}/view")
    public String viewTask(@PathVariable Long taskId, Model model) {
        Task task = taskUseCase.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TaskResponseDto dto = modelMapper.map(task, TaskResponseDto.class);

        dto.setComments(commentLookup.getCommentsForTask(taskId));

        Long currentUserId = userAuthHelper.getCurrentUserId();

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("task", dto);
        return "task/taskPage";
    }
    // 1. Add the Accept Logic
    @PostMapping("/{taskId}/accept")
    public String acceptTask(@PathVariable Long taskId) {
        taskUseCase.acceptTask(taskId);
        return "redirect:/tasks/" + taskId + "/view";
    }

    // 2. Add the Complete Logic
    @PostMapping("/{taskId}/complete")
    public String completeTask(@PathVariable Long taskId) {
        taskUseCase.completeTask(taskId);
        return "redirect:/tasks/" + taskId + "/view";
    }
}
