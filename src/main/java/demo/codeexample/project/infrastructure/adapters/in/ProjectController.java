package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;
    private final ModelMapper modelMapper;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String dashboard(@ModelAttribute("company") String companyName, Model model) {
        Long userId = getCurrentUserId();

        model.addAttribute("projects", projectUseCase.findProjectsForUser(userId));
        model.addAttribute("company", companyName);

        userLookup.findById(userId)
                .ifPresent(user -> model.addAttribute("currentUser", user));

        return "producer/dashboard";
    }

    @GetMapping("/projects/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
    public String createProjectPage(@ModelAttribute("company") String companyName, Model model) {
        var users = userLookup.findAll();
        model.addAttribute("users", users);
        model.addAttribute("company", companyName);

        Long userId = getCurrentUserId();
        userLookup.findById(userId)
                .ifPresent(user -> model.addAttribute("currentUser", user));

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

        Long userId = getCurrentUserId();
        userLookup.findById(userId)
                .ifPresent(user -> model.addAttribute("currentUser", user));

        return "project-details";
    }

    @PostMapping("/projects")
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
    public String createProject(@ModelAttribute("projectDto") @Valid CreateProjectDto projectDto) {
        projectUseCase.createProject(projectDto);
        return "redirect:/{company}/dashboard";
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof Long userId) {
            return userId;
        }

        throw new IllegalStateException("Unsupported principal type");
    }
}