package demo.codeexample.company;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCompanyDto {
    @NotBlank(message = "Your company must have a name")
    private String companyName;

    private Address address;

    @NotBlank(message = "Users must have a way to contact you")
    private String email;
}
