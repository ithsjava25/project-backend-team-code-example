package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.ProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.user.UserLookup;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final UserLookup userLookup;

    @GetMapping("/dashboard")
    public String dashboard(@ModelAttribute("company") String companyName, Model model) {
        var projects = projectUseCase.findAllProjectsFromCompany(companyName);

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
    @ResponseBody // Vi svarar med JSON så JavaScriptet kan läsa ID:t
    public ResponseEntity<?> createProject(@ModelAttribute @Valid CreateProjectDto dto) {
        try {
            // 1. Skapa projektet via ditt UseCase
            // VIKTIGT: Se till att createProject returnerar det SPARADE objektet
            var newProject = projectUseCase.createProject(
                    dto.title(), dto.description(), dto.releaseDate(),
                    dto.employeesId() != null ? dto.employeesId() : Set.of(),
                    dto.category(), dto.genre(), dto.companyId(),
                    dto.recruitingDeadline(), dto.recordingDeadline(), dto.editingDeadline()
            );

            // 2. Kontrollera att vi fick ett ID
            if (newProject == null || newProject.getId() == null) {
                return ResponseEntity.status(500).body("Projektet sparades men fick inget ID. Kolla databasen.");
            }


            // 3. Skicka tillbaka ID:t till JavaScriptet
            return ResponseEntity.ok(Map.of("id", newProject.getId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Java Error: " + e.getMessage());
        }

//   public String createProject(@ModelAttribute("projectDto") @Valid CreateProjectDto projectDto) {
//   projectUseCase.createProject(projectDto);
//   return "redirect:/{company}/dashboard";
// }
}

