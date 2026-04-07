package demo.codeexample.logger;

import demo.codeexample.enums.LoggerAction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoggerDto {

    private Long id;

    private LoggerAction action;

    private String message;

    private Long userId;
    private String userName;

    private Long projectId;
    private String projectTitle;

    private String entityType;
    private Long entityId;

    private LocalDateTime createdAt;
}