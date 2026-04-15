package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.application.in.ProjectUseCase;
import demo.codeexample.project.application.usecase.ProjectService;

public class ProjectController {

    ProjectUseCase projectUseCase;

    public ProjectController(ProjectUseCase projectUseCase) {
        this.projectUseCase = projectUseCase;

    }
}
