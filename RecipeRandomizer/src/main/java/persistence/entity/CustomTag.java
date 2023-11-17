package persistence.entity;


import lombok.Data;
import lombok.ToString;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @ManyToMany(mappedBy = "customTags", fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private List<Recipe> recipes;

    public CustomTag() {
        recipes = new ArrayList<>();
    }

    public CustomTag(String name, User user) {
        this.name = name;
        this.user = user;
        recipes = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe) {
        recipe.addCustomTag(this);
    }

    public void removeRecipeById(long recipeId) {
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
