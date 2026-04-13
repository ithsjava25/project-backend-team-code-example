package demo.codeexample.logger.application;

import demo.codeexample.logger.LoggerLookup;
import demo.codeexample.logger.LoggerAction;
import demo.codeexample.logger.domain.LoggerEntity;
import demo.codeexample.logger.domain.LoggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class LoggerService implements LoggerLookup{

    private final LoggerRepository loggerRepository;

    public void log(LoggerAction action, Long userId, String entityType, Long entityId) {
        LoggerEntity log = new LoggerEntity();
        log.setAction(action);
        log.setUserId(userId);
        log.setEntityType(entityType);
        log.setEntityId(entityId);

        loggerRepository.save(log);
    }

}
