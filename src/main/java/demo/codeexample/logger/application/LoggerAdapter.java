package demo.codeexample.logger.application;

import demo.codeexample.logger.domain.LoggerAction;
import demo.codeexample.logger.LoggerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoggerAdapter implements LoggerPort {

    private LoggerService loggerService;

    @Override
    public void log(LoggerAction action, Long userId, String entityType, Long entityId) {

        loggerService.log(action, userId, entityType, entityId);
    }
}
