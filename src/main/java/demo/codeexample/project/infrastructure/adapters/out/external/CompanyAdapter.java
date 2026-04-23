package demo.codeexample.project.infrastructure.adapters.out.external;

import demo.codeexample.company.CompanyLookup;
import demo.codeexample.project.application.out.CompanyPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompanyAdapter implements CompanyPort {

    private final CompanyLookup lookup;

    @Override
    public Long getCompanyIdFromName(String companyName) {
        return lookup.getCompanyIdFromName(companyName);
    }
}
