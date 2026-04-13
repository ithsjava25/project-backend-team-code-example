package demo.codeexample.project.infrastructure;

import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.application.usecase.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ProjectService projectService(ProjectRepositoryPort projectRepositoryPort, UserPort userPort, ModelMapper ModelMapper) {
        return new ProjectService(projectRepositoryPort, userPort, ModelMapper);
    }

}
