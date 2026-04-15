package demo.codeexample.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
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

    @Column(nullable = false)
    private Long taskId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Comment(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }


}
