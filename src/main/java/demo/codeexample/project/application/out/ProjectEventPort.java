package demo.codeexample.project.application.out;

import demo.codeexample.project.ProjectCreatedEvent;

public interface ProjectEventPort {
    void publish(ProjectCreatedEvent event);
}
