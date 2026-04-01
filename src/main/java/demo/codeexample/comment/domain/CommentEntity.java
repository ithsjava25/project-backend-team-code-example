package demo.codeexample.comment.domain;

import demo.codeexample.task.TaskEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 300)
    private String content;

//    @Column(nullable = false)
//    private Long writerId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CommentEntity(String content  ) {
        this.content = content;
        //.writerId = writerId;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
