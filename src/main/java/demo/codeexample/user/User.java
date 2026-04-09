package demo.codeexample.user;

import demo.codeexample.user.doman.Role;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import java.time.LocalDateTime;
import java.util.Objects;



//How user looks in database
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email; // this is the login identifier (work email)

    @Column(nullable = false)
    private String password; // will be BCrypt hashed - never plain text

    @Enumerated(EnumType.STRING) // Stores "ADMIN" not "0" in DB - readable
    @Column(nullable = false)
    private Role role; // Enum PRODUCER, DIRECTOR, RECRUITER, EDITOR, VISITOR

    @Column(nullable = false)
    private boolean active = true;              // Can be deactivated by admin

    @Column(nullable = false)
    private boolean passwordResetRequired = true; // Force change on first login

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User that = (User) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
