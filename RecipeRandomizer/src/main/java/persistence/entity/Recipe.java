package persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "recipes", schema = "recipe_randomizer")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "meal_category_id", nullable = false)
    private MealCategory mealCategory;

    @ManyToOne
    @JoinColumn(name = "regional_cuisine_id")
    private RegionalCuisine regionalCuisine;

    @Column(name = "cooking_time")
    private Integer cookingTimeMin;

    @Basic
    private Integer portions;

    @Basic
    private Integer calories;

    @Lob
    @Basic (fetch = FetchType.LAZY)
    @Column(name = "image_data", columnDefinition = "mediumblob")
    private byte[] imageData;

    @Lob
    @Basic (fetch = FetchType.LAZY)
    @Column(name = "recipe_text", columnDefinition = "nvarchar(3000)", nullable = false)
    private String recipeText;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "recipes_dishes_by_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_by_ingredients_id"))
    private List<DishByIngredients> dishesByIngredients;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "recipes_common_allergens",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id"))
    private List<Allergen> allergens;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "recipes_custom_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "custom_tag_id"))
    private List<CustomTag> customTags;


    public Recipe() {
        dishesByIngredients = new ArrayList<>();
        allergens = new ArrayList<>();
        customTags = new ArrayList<>();
    }

    public Recipe(String name, MealCategory mealCategory, String recipeText, User user) {
        this.user = user;
        this.name = name;
        this.mealCategory = mealCategory;
        this.recipeText = recipeText;
        dishesByIngredients = new ArrayList<>();
        allergens = new ArrayList<>();
        customTags = new ArrayList<>();
    }

    public void addDishByIngredients(DishByIngredients dishByIngredients) {
        this.dishesByIngredients.add(dishByIngredients);
        dishByIngredients.getRecipes().add(this);
    }

    public void addAllergen(Allergen allergen) {
        this.allergens.add(allergen);
        allergen.getRecipes().add(this);
    }

    public void addCustomTag(CustomTag customTag) {
        this.customTags.add(customTag);
        customTag.getRecipes().add(this);
    }

    public void removeCustomTagById(long customTagId) {
        CustomTag customTag = this.customTags.stream()
                .filter(c -> c.getId() == customTagId).findFirst().orElse(null);
        if (customTag != null) {
            this.customTags.remove(customTag);
            customTag.getRecipes().remove(this);
        }
    }

    public void removeDishByIngredients(DishByIngredients dishByIngredients) {
        if (dishByIngredients != null) {
            this.dishesByIngredients.remove(dishByIngredients);
            dishByIngredients.getRecipes().remove(this);
        }
    }

    public void removeDishByIngredientsById(long dishByIngredientsId) {
        DishByIngredients dishByIngredients = this.dishesByIngredients.stream()
                .filter(d -> d.getId() == dishByIngredientsId).findFirst().orElse(null);
        if (dishByIngredients != null) {
            this.dishesByIngredients.remove(dishByIngredients);
            dishByIngredients.getRecipes().remove(this);
        }
    }

    public void removeAllergenById(long allergenId) {
        Allergen allergen = this.allergens.stream()
                .filter(a -> a.getId() == allergenId).findFirst().orElse(null);
        if (allergen != null) {
            this.allergens.remove(allergen);
            allergen.getRecipes().remove(this);
        }
    }

    @Override
    public String toString(){
        return "Recipe paameters: " + id + " " + name + " " + mealCategory.getName();
    }


}
