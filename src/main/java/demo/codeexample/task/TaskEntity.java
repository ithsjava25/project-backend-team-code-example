package demo.codeexample.task;

import demo.codeexample.employee.EmployeeEntity;
import demo.codeexample.enums.TaskStatus;
import demo.codeexample.project.ProjectEntity;
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
    private Long TaskId;

    private String Title;

    private String Description;

    @Enumerated(EnumType.STRING)
    private TaskStatus Status;

    private LocalDateTime Deadline;

    private Long ProjectId;

    @ManyToOne
    @JoinColumn(name = "Employee_id")
    private EmployeeEntity Employee;


    public TaskEntity (Long TaskId, String Title, String Description, TaskStatus Status, LocalDateTime Deadline, Long ProjectId) {
        this.TaskId = TaskId;
        this.Title = Title;
        this.Description = Description;
        this.Status = Status;
        this.Deadline = Deadline;
        this.ProjectId = ProjectId;
        this.Employee = new EmployeeEntity();
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
