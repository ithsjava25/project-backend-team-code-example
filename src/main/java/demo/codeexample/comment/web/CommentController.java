package demo.codeexample.comment.web;


import demo.codeexample.comment.CommentDto;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.comment.application.CommentService;
import demo.codeexample.company.TenantContext;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String create(@ModelAttribute CreateCommentDto createCommentDto,
                         RedirectAttributes redirectAttributes){
        commentService.createComment(createCommentDto);

        // Add the ID to the redirect attributes
        redirectAttributes.addAttribute("taskId", createCommentDto.getTaskId());

        return "redirect:/" + TenantContext.getTenant() + "/{taskId}/view";
    }

    @GetMapping
    public String list(Model model){
        model.addAttribute("comments", commentService.getAllComments() );
        return "comment/commentList";
    }
}
