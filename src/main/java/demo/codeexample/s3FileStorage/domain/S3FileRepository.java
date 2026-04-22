package demo.codeexample.s3FileStorage.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileRepository extends JpaRepository<S3File, Long> {


}
