package persistence.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users", schema = "recipe_randomizer")
public class User {
    public enum Role {
        ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Character[] password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Recipe> recipes;

    public User() {
        recipes = new ArrayList<>();
    }

    public User(String name, Character[] password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
        recipes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("ID of user: %1s, name \"%2s\", role: %3s", id, name, role);
    }

    public void addToRecipes(Recipe recipe) {
        recipes.add(recipe);
    }

}
