package demo.codeexample.company.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c.id FROM Company c WHERE c.companyName = :companyName")
    Long getCompanyIdFromName(String companyName);
}
