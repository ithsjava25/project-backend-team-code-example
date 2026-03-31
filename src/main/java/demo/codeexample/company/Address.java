package demo.codeexample.company;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Address {
    private String city;
    private String street;
    private String streetNumber;
    private String zipcode;
}
