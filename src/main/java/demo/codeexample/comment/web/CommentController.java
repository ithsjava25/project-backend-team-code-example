package demo.codeexample.comment.web;


import demo.codeexample.comment.CommentDto;
import demo.codeexample.comment.CreateCommentDto;
import demo.codeexample.comment.application.CommentService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/new")
    public String createCom(Model model) {
        model.addAttribute("comment", new CreateCommentDto());
        return "comment/commentForm";
    }
    @PostMapping
    public String create(@ModelAttribute CreateCommentDto createCommentDto){
        commentService.createComment(createCommentDto);
        return "redirect:/comments";
    }

    @GetMapping
    public String list(Model model){
        model.addAttribute("comments", commentService.getAllComments() );
        return "comment/commentList";
    }
//    @PostMapping
//    public String createComment(
//            @PathVariable Long taskId,
//            @Valid @ModelAttribute CreateCommentDto dto,
//            BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            return "task/?";
//        }
//
//        commentService.createComment(taskId, dto);
//        return "redirect:/tasks/" + taskId;
//    }
}
