package demo.codeexample.logger.application;

import demo.codeexample.logger.LoggerPort;
import demo.codeexample.logger.LoggerAction;
import demo.codeexample.logger.domain.LoggerEntity;
import demo.codeexample.logger.domain.LoggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggerService implements LoggerPort{

    private final LoggerRepository loggerRepository;

    @Override
    public void log(LoggerAction action, Long userId, String entityType, Long entityId) {
        LoggerEntity log = new LoggerEntity();
        log.setAction(action);
        log.setUserId(userId);
        log.setEntityType(entityType);
        log.setEntityId(entityId);

        loggerRepository.save(log);
    }

}
