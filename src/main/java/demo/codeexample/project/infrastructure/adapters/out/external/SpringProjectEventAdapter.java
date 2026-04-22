package demo.codeexample.project.infrastructure.adapters.out.external;

import demo.codeexample.project.ProjectCreatedEvent;
import demo.codeexample.project.application.out.ProjectEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringProjectEventAdapter implements ProjectEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(ProjectCreatedEvent event) {

        publisher.publishEvent(event);
    }
}