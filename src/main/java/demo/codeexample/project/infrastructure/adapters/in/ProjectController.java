package demo.codeexample.project.infrastructure.adapters.in;

import demo.codeexample.project.application.usecase.ProjectService;

public class ProjectController {

    ProjectService service;

    public ProjectController(ProjectService service){
        this.service = service;

    }
}
