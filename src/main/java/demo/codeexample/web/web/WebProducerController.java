package demo.codeexample.web.web;

import demo.codeexample.project.CreateProjectDto;
import demo.codeexample.project.application.usecase.ProjectService;
import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/web/producer")
@RequiredArgsConstructor
public class WebProducerController {

    private final TemplateEngine templateEngine;
    private final ProjectService projectService;

    @GetMapping("/dashboard")
    @ResponseBody
    public String producerDashboard() {

        var projects = projectService.findAllProjects(); // ← this must return DTOs

        return render("producer/producer-dashboard.jte", Map.of(
                "projects", projects
        ));
    }


    @GetMapping("/projects/new")
    @ResponseBody
    public String createProjectPage() {
        return render("producer/create-project.jte", Map.of(
                "categories", Category.values(),
                "genres", Genre.values()
        ));
    }

    private String render(String template, Map<String, Object> params) {
        var output = new StringOutput();
        templateEngine.render(template, params, output);
        return output.toString();
    }

    @PostMapping("/projects")
    public String createProject(CreateProjectDto dto) {
        System.out.println("DTO received: " + dto);

        projectService.createProject(
                dto.title(),
                dto.description(),
                LocalDate.now(),   // temporary default
                Set.of(),          // no employees yet
                dto.category(),
                dto.genre(),
                null,              // no file yet
                1L                 // temporary company ID
        );;

        return "redirect:/web/producer/dashboard";
    }




}