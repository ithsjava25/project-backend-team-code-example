package demo.codeexample.s3FileStorage.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface S3FileRepository extends JpaRepository<S3File, Long> {


    List<S3File> findAllByProjectId(Long projectId);

    Optional<S3File> findFirstByProjectIdAndFileKeyContainingIgnoreCase(Long projectId, String searchTerm);

    void deleteByFileKey(String key);
}
