package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.auth.application.CurrentUserProvider;
import demo.codeexample.company.TenantContext;
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

import java.util.Set;

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
        var projects = projectUseCase.findAllProjectsFromCompany(companyName);

        Long userId = currentUserProvider.getCurrentUserId();
        model.addAttribute("projects", projects, projectUseCase.findProjectsForUser(userId);
        return "producer/dashboard";
    }

//    @GetMapping("/dashboard")
//    @PreAuthorize("hasAnyRole('ADMIN','DIRECTOR','PRODUCER','RECRUITER','EDITOR')")
//    public String dashboard(Model model) {
//        Long userId = currentUserProvider.getCurrentUserId();
//        model.addAttribute("projects", projectUseCase.findProjectsForUser(userId));
//        return "producer/producer-dashboard";
//    }


    @GetMapping("/projects/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
    public String createProject(@ModelAttribute("projectDto") @Valid CreateProjectDto projectDto) {
        projectUseCase.createProject(projectDto);
        return "redirect:/{company}/dashboard";
    }






//    @PostMapping("/projects")
//    @Transactional
//    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'PRODUCER', 'RECRUITER', 'EDITOR')")
//    public String createProject(@ModelAttribute @Valid CreateProjectDto dto) {
//
//        projectUseCase.createProject(
//                dto.title(),
//                dto.description(),
//                dto.releaseDate(),
//                dto.employeesId() != null ? dto.employeesId() : Set.of(),
//                dto.category(),
//                dto.genre(),
//                dto.companyId(),
//                dto.recruitingDeadline(),
//                dto.recordingDeadline(),
//                dto.editingDeadline()
//        );
//
////        return "redirect:/producer/dashboard";
//
//        String company = TenantContext.getTenant();
//        String prefix = (company != null && !company.isBlank()) ? "/" + company : "";
//        return "redirect:" + prefix + "/dashboard";
//
}