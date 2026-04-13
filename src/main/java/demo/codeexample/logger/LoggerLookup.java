package demo.codeexample.logger;

import demo.codeexample.logger.LoggerAction;

public interface LoggerLookup {
    void log(LoggerAction action,
             Long userId,
             String entityType,
             Long entityId);

}
