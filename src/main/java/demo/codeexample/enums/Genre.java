package demo.codeexample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {
    ACTION("Action"),
    COMEDY("Comedy"),
    CRIMINAL("Criminal"),
    DRAMA("Drama"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi");

    private final String displayName;

    @Override
    public String toString(){
        return displayName;
    }
}
