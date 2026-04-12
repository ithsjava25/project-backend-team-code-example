package demo.codeexample.logger;

public interface LoggerPort {
    void log(LoggerAction action,
             Long userId,
             String entityType,
             Long entityId);

}
