package demo.codeexample.comment.web;


import demo.codeexample.comment.CommentDto;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.comment.application.CommentService;
import demo.codeexample.company.TenantContext;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{taskId}/comments")
    public String create(@PathVariable Long taskId,
                         @Valid @ModelAttribute CreateCommentDto createCommentDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        createCommentDto.setTaskId(taskId);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("taskId", taskId);
            return "redirect:/tasks/{taskId}/view";
        }

        commentService.createComment(createCommentDto);

        redirectAttributes.addAttribute("taskId", taskId);
        return "redirect:/{company}/tasks/{taskId}/view";
    }
}
