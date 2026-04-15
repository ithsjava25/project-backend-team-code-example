package demo.codeexample.project.infrastructure.adapters.out.persistence;

import demo.codeexample.project.domain.Category;
import demo.codeexample.project.domain.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private LocalDate releaseDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "project_employees", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "employee_id")
    private Set<Long> employeesId = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Long fileId;

    @Column(nullable = false)
    private Long companyId;

    public ProjectEntity(String title, String description, LocalDate releaseDate,
                         Set<Long> employeesId, Category category, Genre genre,
                         Long fileId, Long companyId){
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.employeesId = employeesId;
        this.category = category;
        this.genre = genre;
        this.fileId = fileId;
        this.companyId = companyId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProjectEntity that = (ProjectEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
