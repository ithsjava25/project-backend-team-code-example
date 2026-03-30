package demo.codeexample.task;

import demo.codeexample.employee.EmployeeEntity;
import demo.codeexample.enums.TaskStatus;
import demo.codeexample.project.ProjectEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private TaskStatus Status;

    private LocalDateTime Deadline;

    private Long ProjectId;

    @ManyToOne
    @JoinColumn(name = "Employee_id")
    private EmployeeEntity Employee;





}
