package demo.codeexample.project.infrastructure;

import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.usecase.ProjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ProjectService projectService(ProjectRepositoryPort projectRepositoryPort){
        return new ProjectService(projectRepositoryPort);
    }

}
