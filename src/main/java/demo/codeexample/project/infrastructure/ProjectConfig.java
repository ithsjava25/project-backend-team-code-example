package demo.codeexample.project.infrastructure;

import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.project.TaskLookup;
import demo.codeexample.project.application.out.CompanyPort;
import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.project.application.out.ProjectEventPort;
import demo.codeexample.project.application.out.ProjectRepositoryPort;
import demo.codeexample.project.application.out.SecurityPort;
import demo.codeexample.project.application.out.UserPort;
import demo.codeexample.project.application.usecase.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ProjectService projectService(ProjectRepositoryPort projectRepositoryPort, UserPort userPort,
                                         ProjectEventPort projectEventPort, SecurityPort securityPort,
                                         ModelMapper modelMapper, LoggerLookup logger, TaskLookup taskLookup) {

        return new ProjectService(projectRepositoryPort, userPort, projectEventPort, securityPort, modelMapper, logger,  taskLookup);
    }

}
