package demo.codeexample.logger.domain;


import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerRepository extends JpaRepository<LoggerEntity, Long> {
}