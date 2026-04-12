package demo.codeexample.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 300)
    private String content;

    @Column
    private Long TaskId;

//    @Column(nullable = false)
//    private Long writerId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Comment(String content  ) {
        this.content = content;
        //.writerId = writerId;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
