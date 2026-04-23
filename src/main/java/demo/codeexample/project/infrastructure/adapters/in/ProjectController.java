package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;
    private final CurrentUserLookup currentUserLookup;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String dashboard(@ModelAttribute("company") String companyName, Model model) {
        var currentUser = currentUserLookup.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("No authenticated user"));

        model.addAttribute("projects", projectUseCase.findProjectsForUser(currentUser.getId()));
        model.addAttribute("company", companyName);

        return "producer/dashboard";
    }

    @GetMapping("/projects/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
    public String createProjectPage(@ModelAttribute("company") String companyName, Model model) {
        model.addAttribute("users", userLookup.findAll());
        model.addAttribute("company", companyName);

        return "producer/create-project";
    }

    @GetMapping("/dashboard/{title}")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String showProjectDetails(@PathVariable String title,
                                     @RequestParam Long projectId,
                                     @ModelAttribute("company") String companyName,
                                     Model model) {
        ProjectDto currentProject = projectUseCase.getProjectDetails(projectId);

        model.addAttribute("currentProject", currentProject);
        model.addAttribute("company", companyName);

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