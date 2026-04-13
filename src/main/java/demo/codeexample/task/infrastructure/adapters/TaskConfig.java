package demo.codeexample.task.infrastructure.adapters;

import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.task.application.ports.out.TaskRepositoryPort;
import demo.codeexample.task.application.ports.usecase.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

    @Bean
    public TaskService taskService(TaskRepositoryPort taskRepositoryPort, LoggerLookup loggerPort) {
        return new TaskService(taskRepositoryPort, loggerPort);
    }

}
