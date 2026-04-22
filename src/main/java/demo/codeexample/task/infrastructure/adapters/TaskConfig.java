package demo.codeexample.task.infrastructure.adapters;

import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.task.application.ports.in.TaskUseCase;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.application.ports.usecase.TaskService;
import demo.codeexample.task.infrastructure.adapters.in.ProjectEventListenerAdapter;
import demo.codeexample.user.UserLookup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class TaskConfig {

    @Bean
    public TaskService taskService(TaskRepositoryPort taskRepositoryPort, UserLookup userLookup, LoggerLookup logger) {
        return new TaskService(taskRepositoryPort, userLookup, logger);
    }
    @Bean
    public ProjectEventListenerAdapter projectEventListenerAdapter(TaskUseCase taskUseCase) {
        return new ProjectEventListenerAdapter(taskUseCase);
    }

}
