package demo.codeexample.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    FILM("Film"),
    SERIES("Series");

    private final String displayName;

    @Override
    public String toString(){
        return displayName;
    }
}
