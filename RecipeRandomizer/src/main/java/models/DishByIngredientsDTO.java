package models;

import lombok.Data;
import persistence.entity.DishByIngredients;


@Data
public class DishByIngredientsDTO {
    private long id;
    private String name;
    public DishByIngredientsDTO(DishByIngredients dishByIngredients) {
        this.id = dishByIngredients.getId();
        this.name = dishByIngredients.getName();
    }
}
