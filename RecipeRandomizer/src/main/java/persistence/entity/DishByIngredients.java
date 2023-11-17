package persistence.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "dishes_by_ingredients", schema = "recipe_randomizer")
public class DishByIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "dishesByIngredients", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private List<Recipe> recipes;

    public DishByIngredients() {
        recipes = new ArrayList<>();
    }

    public DishByIngredients(String name) {
        this.name = name;
        recipes = new ArrayList<>();
    }

    public void addRecipe (Recipe recipe) {
        recipe.addDishByIngredients(this);
    }

    public void removeRecipeById (long recipeId) {
        this.recipes.stream()
                .filter(recipe -> recipe.getId() == recipeId)
                .findFirst()
                .ifPresent(recipe -> recipe.removeDishByIngredientsById(this.getId()));
    }


    @Override
    public String toString() {
        return String.format("ID типу інгрідієнтів: %1s, назва \"%2s\";", id, name);
    }


}
