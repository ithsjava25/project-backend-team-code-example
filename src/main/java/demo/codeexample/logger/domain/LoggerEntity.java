package demo.codeexample.logger.domain;

import demo.codeexample.shared.LoggerAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "logger")
public class LoggerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "logger_action")
    private LoggerAction action;

    private String message;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "entity_type")
    private String entityType;
    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
