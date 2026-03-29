package demo.codeexample.task;

import demo.codeexample.employee.EmployeeEntity;
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
    private Long id;

    @Column
    private String Title;
    @Column
    private String Description;
    @Column
    private String Status;
    @Column
    private LocalDateTime Deadline;

    @JoinColumn
    @ManyToOne
    private ProjectEntity Project;

    @JoinColumn
    @ManyToOne
    private EmployeeEntity Employee;




}
