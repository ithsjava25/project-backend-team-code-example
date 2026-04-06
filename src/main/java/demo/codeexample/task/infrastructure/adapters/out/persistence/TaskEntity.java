package demo.codeexample.task.infrastructure.adapters.out.persistence;

import demo.codeexample.enums.TaskStatus;
import demo.codeexample.project.infrastructure.adapters.out.ProjectEntity;
import demo.codeexample.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "Task_id")
    private Long taskId;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn (name = "project_id")
    ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;


    public TaskEntity (String title, String description, TaskStatus status, LocalDateTime deadline, ProjectEntity project, UserEntity user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.project = project;
        this.user = user;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TaskEntity that = (TaskEntity) o;
        return getTaskId() != null && Objects.equals(getTaskId(), that.getTaskId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
