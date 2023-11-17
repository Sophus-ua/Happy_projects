package persistence.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "regional_cuisines", schema = "recipe_randomizer")
public class RegionalCuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "regionalCuisine")
    private List<Recipe> recipes;

    public RegionalCuisine(){
        recipes = new ArrayList<>();
    }
    public RegionalCuisine(String name){
        this.name = name;
        recipes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("ID регіональної кухні: %1s, назва \"%2s\";", id, name);
    }

    public void addToRecipes(Recipe recipe){
        recipes.add(recipe);
    }
}
