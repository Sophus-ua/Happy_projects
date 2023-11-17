package persistence.entity;


import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users", schema = "recipe_randomizer")
public class User {
    public enum Role {
        ADMIN, MODERATOR, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", length = 100, nullable = false, unique = true, columnDefinition = "NVARCHAR(100) COLLATE utf8_general_ci")
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(name = "own_name", length = 100, nullable = false)
    private String ownName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter
    @Column(name = "enabled",nullable = false)
    private boolean enabled;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Recipe> recipes;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<CustomTag> customTags;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<ImageBuffer> imageBuffers;

    public User() {
        recipes = new ArrayList<>();
    }

    public User(String username, String password, String ownName) {
        this.username = username;
        this.password = password;
        this.ownName = ownName;
        this.role = Role.USER;
        this.enabled = true;
        registrationDate = LocalDate.now();
        recipes = new ArrayList<>();
        customTags = new ArrayList<>();
        imageBuffers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("User name: \"%1s\", role: %2s", ownName, role);
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void addCustomTag(CustomTag customTag) {
        customTags.add(customTag);
    }

    public void addImageBuffer(ImageBuffer imageBuffer) {
        imageBuffers.add(imageBuffer);
    }
}
