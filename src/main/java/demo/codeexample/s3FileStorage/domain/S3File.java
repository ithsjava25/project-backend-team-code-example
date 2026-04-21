package demo.codeexample.s3FileStorage.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="s3")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class S3File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false, unique = true)
    private String fileKey;

    @Column(nullable = false)
    private String contentType;

}
