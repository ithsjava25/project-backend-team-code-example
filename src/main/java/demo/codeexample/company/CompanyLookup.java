package demo.codeexample.company;

import java.util.Optional;

public interface CompanyLookup {
    Long getCompanyIdFromName(String companyName);

//    Optional<CompanyDto> findById(Long id);
//
//    Optional<CompanyDto> findByTenant(String tenant);
//
//    record CompanyDto(Long id, String companyName, String tenant) {}

}