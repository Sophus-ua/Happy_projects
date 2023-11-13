package models;

import lombok.Data;
import persistence.entity.RegionalCuisine;

@Data
public class RegionalCuisineDTO {
    private long id;
    private String name;
    public RegionalCuisineDTO(RegionalCuisine regionalCuisine) {
        this.id = regionalCuisine.getId();
        this.name = regionalCuisine.getName();
    }
}
