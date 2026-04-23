package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.security.CurrentUserService;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;
    private final ModelMapper modelMapper;
    private final CurrentUserService currentUserService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("projects", projectUseCase.findAllProjects());

        currentUserService.getCurrentUser()
                .ifPresent(user -> model.addAttribute("currentUser", user));

        return "producer/producer-dashboard";
    }

    @GetMapping("/projects/new")
    public String createProjectPage(Model model) {
        var users = userLookup.findAll();
        model.addAttribute("users", users);
        return "producer/create-project";
    }

    @PostMapping("/projects")
    @Transactional
    public String createProject(@ModelAttribute @Valid CreateProjectDto dto) {

        projectUseCase.createProject(
                dto.title(),
                dto.description(),
                dto.releaseDate(),
                dto.employeesId() != null ? dto.employeesId() : Set.of(),
                dto.category(),
                dto.genre(),
                dto.companyId(),
                dto.recruitingDeadline(),
                dto.recordingDeadline(),
                dto.editingDeadline()
        );

        return "redirect:/producer/dashboard";
    }
}

