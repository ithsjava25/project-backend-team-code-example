package demo.codeexample.logger;

import demo.codeexample.enums.LoggerAction;

public interface LoggerPort {
    void log(LoggerAction action,
             Long userId,
             String entityType,
             Long entityId);

}
