package demo.codeexample.user;

import demo.codeexample.enums.Role;
import jakarta.persistence.*;
import lombok.*;


//How user looks in database
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; // Enum PRODUCER, DIRECTOR, RECRUITER, EDITOR, VISITOR

    private String password;


//    //Relationships
//    @OneToMany(mappedBy = "assignedEmployees")
//    private List<TaskEntity> tasks;

}
