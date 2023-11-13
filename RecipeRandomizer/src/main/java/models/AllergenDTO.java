package models;

import lombok.Data;
import persistence.entity.Allergen;


@Data
public class AllergenDTO {
    private long id;
    private String name;
    public AllergenDTO(Allergen allergen) {
        this.id = allergen.getId();
        this.name = allergen.getName();
    }
}
