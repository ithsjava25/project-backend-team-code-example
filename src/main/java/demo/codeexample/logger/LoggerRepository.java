package demo.codeexample.logger;


import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggerRepository extends JpaRepository<LoggerEntity, Long> {
}