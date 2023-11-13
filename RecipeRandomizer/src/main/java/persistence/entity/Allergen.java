package persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "common_allergens", schema = "recipe_randomizer")
public class Allergen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;


    @ManyToMany(mappedBy = "allergens", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private List<Recipe> recipes;

    public Allergen() {
        recipes = new ArrayList<>();
    }

    public Allergen(String name) {
        this.name = name;
        recipes = new ArrayList<>();
    }

    public void addRecipe (Recipe recipe) {
        recipe.addAllergen(this);
    }

    public void removeRecipeById (long recipeId) {
        this.recipes.stream()
                .filter(recipe -> recipe.getId() == recipeId)
                .findFirst()
                .ifPresent(recipe -> recipe.removeAllergenById(this.id));
    }

    @Override
    public String toString() {
        return String.format("ID алергену: %1s, назва \"%2s\";", id, name);
    }


}
