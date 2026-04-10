package demo.codeexample.logger.application;

import demo.codeexample.logger.domain.LoggerAction;
import demo.codeexample.logger.LoggerLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class LoggerAdapter implements LoggerLookup {

    private final LoggerService loggerService;

    @Override
    public void log(LoggerAction action, Long userId, String entityType, Long entityId) {

        loggerService.log(action, userId, entityType, entityId);
    }
}
