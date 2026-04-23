package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.auth.application.CurrentUserProvider;
import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;
    private final ModelMapper modelMapper;
    private final CurrentUserProvider currentUserProvider;



    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String dashboard(@ModelAttribute("company") String companyName, Model model) {
        Long userId = currentUserProvider.getCurrentUserId();

        model.addAttribute("projects", projectUseCase.findProjectsForUser(userId));
        userLookup.findById(userId)
                .ifPresent(user -> model.addAttribute("currentUser", user));

        return "producer/dashboard";
    }


    @GetMapping("/projects/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
    public String createProjectPage(Model model) {
        var users = userLookup.findAll();
        model.addAttribute("users", users);

        return "producer/create-project";
    }


    @GetMapping("/dashboard/{title}")
    public String showProjectDetails(@PathVariable String title, @RequestParam Long projectId, Model model) {
        ProjectDto currentProject = projectUseCase.getProjectDetails(projectId);

        model.addAttribute("currentProject", currentProject);
        return "project-details";
    }


    @PostMapping("/projects")
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
    public String createProject(@ModelAttribute("projectDto") @Valid CreateProjectDto projectDto) {
        projectUseCase.createProject(projectDto);
        return "redirect:/{company}/dashboard";
    }
}