package demo.codeexample.logger;

import demo.codeexample.enums.LoggerAction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoggerDto {

    private Long id;

    private LoggerAction action;

    private String message;

    private String userName;

    private String projectTitle;

    private LocalDateTime createdAt;
}