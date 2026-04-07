package demo.codeexample.logger.application;

import demo.codeexample.enums.LoggerAction;
import demo.codeexample.logger.domain.LoggerEntity;
import demo.codeexample.logger.domain.LoggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggerService {

    private final LoggerRepository loggerRepository;

    public void log(LoggerAction action, String message, Long userId, Long projectId) {
        LoggerEntity log = new LoggerEntity();
        log.setAction(action);
        log.setMessage(message);
        log.setUserId(userId);
        log.setProjectId(projectId);

        loggerRepository.save(log);
    }
}
