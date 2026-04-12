package demo.codeexample.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
        List<CommentEntity> findAllByTaskIdOrderByCreatedAtDesc(Long taskId);

}
