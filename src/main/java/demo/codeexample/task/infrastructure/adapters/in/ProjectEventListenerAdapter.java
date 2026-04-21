package demo.codeexample.task.infrastructure.adapters.in;

import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectEventListenerAdapter {

    private final TaskUseCase taskUseCase;

    @EventListener
    public void handle(ProjectCreatedEvent event) {
        taskUseCase.handleProjectCreated(event);
        }
    }
