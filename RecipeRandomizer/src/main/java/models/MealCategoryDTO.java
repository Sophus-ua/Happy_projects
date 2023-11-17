package models;

import lombok.Data;
import persistence.entity.MealCategory;

@Data
public class MealCategoryDTO {
    private long id;
    private String name;
    public MealCategoryDTO(){}
    public MealCategoryDTO(MealCategory mealCategory) {
        this.id = mealCategory.getId();
        this.name = mealCategory.getName();
    }
}
