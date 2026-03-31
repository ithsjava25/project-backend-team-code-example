package demo.codeexample.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCompanyDto {
    @NotBlank(message = "Your company must have a name")
    private String companyName;

    @NotNull
    private Address address;

    @Email
    @NotBlank(message = "Users must have a way to contact you")
    private String email;
}
