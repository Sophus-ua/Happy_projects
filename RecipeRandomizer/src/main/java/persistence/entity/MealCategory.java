package persistence.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "meal_categories", schema = "recipe_randomizer")
public class MealCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "mealCategory")
    private List<Recipe> recipes;

    public MealCategory(){
        recipes = new ArrayList<>();
    }
    public MealCategory(String name){
        this.name = name;
        recipes = new ArrayList<>();
    }

    public void addToRecipes(Recipe recipe){
        recipes.add(recipe);
    }
    @Override
    public String toString() {
        return String.format("ID категорії прийому їжі: %1s, назва \"%2s\";", id, name);
    }
}
