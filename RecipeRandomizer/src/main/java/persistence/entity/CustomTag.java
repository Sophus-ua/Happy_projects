package persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "custom_tags", schema = "recipe_randomizer")
public class CustomTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;



    @ManyToMany(mappedBy = "customTags", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private List<Recipe> recipes;

    public CustomTag() {
        recipes = new ArrayList<>();
    }

    public CustomTag(String name) {
        this.name = name;
        recipes = new ArrayList<>();
    }

    public void addRecipe (Recipe recipe) {
        recipe.addCustomTag(this);
    }

    public void removeRecipeById (long recipeId) {
        this.recipes.stream()
                .filter(recipe -> recipe.getId() == recipeId)
                .findFirst()
                .ifPresent(recipe -> recipe.removeCustomTagById(this.id));
    }

    @Override
    public String toString() {
        return String.format("ID власного ТАГу: %1s, назва \"%2s\";", id, name);
    }


}
