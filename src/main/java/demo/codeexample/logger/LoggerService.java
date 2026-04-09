package demo.codeexample.logger;

import demo.codeexample.enums.LoggerAction;
import demo.codeexample.project.ProjectEntity;
import demo.codeexample.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggerService {

    private final LoggerRepository loggerRepository;

    public void log(LoggerAction action, String message, User user, ProjectEntity project) {
        LoggerEntity log = new LoggerEntity();
        log.setAction(action);
        log.setMessage(message);
        log.setUser(user);
        log.setProject(project);

        loggerRepository.save(log);
    }
}
