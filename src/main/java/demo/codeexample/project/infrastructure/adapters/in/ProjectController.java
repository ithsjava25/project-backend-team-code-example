package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.out.SecurityPort;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;

    @GetMapping("/dashboard/completed")
    public String dashboardCompletedProjects(@ModelAttribute("company") String companyName, Model model) {
        var projects = projectUseCase.findAllCompletedProjectsByCompany(companyName);

        model.addAttribute("projects", projects);
        return "producer/dashboard";
    }

    @GetMapping("/dashboard/current")
    public String dashboardNotCompletedProjects(@ModelAttribute("company") String companyName, Model model) {
        var projects = projectUseCase.findAllNotCompleteProjectsByCompany(companyName);


        model.addAttribute("projects", projects);
        return "producer/dashboard";
    }

    @GetMapping("/projects/new")
    public String createProjectPage(Model model) {
        var users = userLookup.findAll();
        model.addAttribute("users", users);

        return "producer/create-project";
    }


    @GetMapping("/dashboard/{title}")
    public String showProjectDetails(@PathVariable String title, @RequestParam Long projectId, Model model){
        ProjectDto currentProject = projectUseCase.getProjectDetails(projectId);

        model.addAttribute("currentProject", currentProject);
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
}

