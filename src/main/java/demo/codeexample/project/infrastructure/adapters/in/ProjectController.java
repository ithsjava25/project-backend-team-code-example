package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/web/producer")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final TemplateEngine templateEngine;

    @GetMapping("/dashboard")
    @ResponseBody
    public String dashboard() {
        var projects = projectUseCase.findAllProjects();

        return render("producer/producer-dashboard.jte", Map.of(
                "projects", projects
        ));
    }

    @GetMapping("/projects/new")
    @ResponseBody
    public String createProjectPage() {
        return render("producer/create-project.jte", Map.of(
        ));
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

        return "redirect:/web/producer/dashboard";
    }

    private String render(String template, Map<String, Object> params) {
        var output = new StringOutput();
        templateEngine.render(template, params, output);
        return output.toString();
    }
}

