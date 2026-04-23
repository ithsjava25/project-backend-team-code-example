package demo.codeexample.s3FileStorage.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface S3FileRepository extends JpaRepository<S3File, Long> {


    List<S3File> findAllByProjectId(Long projectId);
}
