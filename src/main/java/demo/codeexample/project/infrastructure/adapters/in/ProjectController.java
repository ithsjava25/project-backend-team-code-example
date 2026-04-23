package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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


    @PostMapping("/projects")
    @Transactional
    public String createProject(@ModelAttribute("projectDto") @Valid CreateProjectDto projectDto) {
        projectUseCase.createProject(projectDto);
        return "redirect:/{company}/dashboard/current";
    }
}

