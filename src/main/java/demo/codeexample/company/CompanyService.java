package demo.codeexample.company;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private final ModelMapper modelMapper;

}
