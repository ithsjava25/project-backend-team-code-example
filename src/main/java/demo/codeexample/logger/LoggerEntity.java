package demo.codeexample.logger;

import demo.codeexample.enums.LoggerAction;
import demo.codeexample.project.ProjectEntity;
import demo.codeexample.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LoggerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoggerAction action;

    private String message;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ProjectEntity project;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
