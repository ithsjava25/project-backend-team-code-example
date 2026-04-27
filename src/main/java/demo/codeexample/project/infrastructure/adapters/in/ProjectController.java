package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.auth.CurrentUserLookup;
import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.TaskLookup;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.task.TaskResponseDto;
import demo.codeexample.task.TaskSummaryDto;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;
    private final CurrentUserLookup currentUserLookup;
    private final TaskLookup taskLookup;

    @GetMapping("/dashboard/completed")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String dashboardCompletedProjects(@ModelAttribute("company") String companyName, Model model) {
        var currentUser = currentUserLookup.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("No authenticated user"));

        model.addAttribute("projects",
                projectUseCase.findCompletedProjectsForUser(currentUser.getId(), companyName));
        model.addAttribute("company", companyName);

        return "producer/dashboard";
    }

    @GetMapping("/dashboard/current")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String dashboardCurrentProjects(@ModelAttribute("company") String companyName, Model model) {
        var currentUser = currentUserLookup.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("No authenticated user"));

        model.addAttribute("projects",
                projectUseCase.findCurrentProjectsForUser(currentUser.getId(), companyName));
        model.addAttribute("company", companyName);

        return "producer/dashboard";
    }

    @GetMapping("/projects/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCER')")
    public String createProjectPage(@ModelAttribute("company") String companyName, Model model) {
        var users = userLookup.findAll();
        model.addAttribute("users", users);
        model.addAttribute("company", companyName);

        return "producer/create-project";
    }


    @GetMapping("/dashboard/{title}")
    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
    public String showProjectDetails(@PathVariable String title,
                                     @RequestParam Long projectId,
                                     @ModelAttribute("company") String companyName,
                                     Model model){
        ProjectDto currentProject = projectUseCase.getProjectDetails(projectId);

        List<TaskSummaryDto> tasks = taskLookup.getTasksByProjectId(projectId);

        model.addAttribute("currentProject", currentProject);
        model.addAttribute("company", companyName);
        model.addAttribute("tasks", tasks);

        return "project-details";
    }

    @GetMapping("/{title}/info/{projectId}")
    public String projectInfo(@PathVariable String title, @PathVariable Long projectId, Model model) {
        try {
            ProjectDto project = projectUseCase.getProjectDetails(projectId);

            model.addAttribute("project", project);
            String companyName = project.getCompanyName();
            model.addAttribute("company", companyName != null ? companyName.toLowerCase(java.util.Locale.ROOT) : "");
        } catch (jakarta.persistence.EntityNotFoundException e) {
            model.addAttribute("project", null);
            model.addAttribute("company", "");

        }

        return "project-movieinfo";
    }

    @PostMapping("/projects")
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCER')")
    @ResponseBody // Respond with JSON so that JavaScriptet can read the ID
    public ResponseEntity<?> createProject(@ModelAttribute("projectDto") @Valid CreateProjectDto dto) {
        try {
            var newProject = projectUseCase.createProject(dto);

            if (newProject == null || newProject.getId() == null) {
                return ResponseEntity.status(500).body("Project was saved without Id. Check database");
            }

            return ResponseEntity.ok(Map.of("id", newProject.getId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Java Error: " + e.getMessage());
        }
    }

    @PostMapping("/projects/{projectId}/finalize")
    public String finalizeProject(@PathVariable Long projectId) {

        projectUseCase.finalizeProject(projectId);

        ProjectDto project = projectUseCase.getProjectDetails(projectId);

        return "redirect:/producer/dashboard/" + project.getTitle() + "?projectId=" + projectId;
    }
}

