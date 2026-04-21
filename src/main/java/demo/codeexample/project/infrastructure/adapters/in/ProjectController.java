package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import gg.jte.TemplateEngine;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final TemplateEngine templateEngine;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var projects = projectUseCase.findAllProjects();

        model.addAttribute("projects", projects);
        return "producer/producer-dashboard";
    }

    @GetMapping("/projects/new")
    public String createProjectPage() {
        return "producer/create-project";
    }

    @PostMapping("/projects")
    public String createProject(@ModelAttribute @Valid CreateProjectDto dto) {

        projectUseCase.createProject(
                dto.title(),
                dto.description(),
                dto.releaseDate(),
                dto.employeesId() != null ? dto.employeesId() : Set.of(),
                dto.category(),
                dto.genre(),
                dto.companyId()
        );

        return "redirect:/producer/dashboard";
    }
}

