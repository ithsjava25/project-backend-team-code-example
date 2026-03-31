package demo.codeexample.company;

import demo.codeexample.project.ProjectDto;
import demo.codeexample.user.UserDTO;
import lombok.Data;

import java.util.Set;

@Data
public class CompanyDto {
    private Long id;
    private String companyName;
    private Address address;
    private String email;
}
