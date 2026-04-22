package demo.codeexample.company.application;

import demo.codeexample.company.CompanyLookup;
import demo.codeexample.company.domain.CompanyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyService implements CompanyLookup {

    private final CompanyRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public Long getCompanyIdFromName(String companyName) {
        return repository.getCompanyIdFromName(companyName);
    }
}
