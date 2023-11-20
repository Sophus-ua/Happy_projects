package models;

import lombok.Data;
import persistence.entity.*;

import java.util.ArrayList;
import java.util.List;


@Data
public class RecipeDTO {
    private Long id;
    private Long commonRecipeId;
    private String name;
    private String username;
    private String mealCategoryName;
    private Long mealCategoryID;
    private String regionalCuisineName;
    private Long regionalCuisineID;
    private Integer cookingTimeMin;
    private Integer portions;
    private Integer calories;
    private List<String> dishesByIngredientsNames;
    private List<Long> dishesByIngredientsIds;
    private List<String> allergensNames;
    private List<Long> allergensIds;
    private List<String> customTagsNames;
    private List<Long> customTagsIds;
    private String[] recipeText;
    private String recipeTextLine;
    private byte[] imageData;

    public RecipeDTO() {
        dishesByIngredientsNames = new ArrayList<>();
        dishesByIngredientsIds = new ArrayList<>();
        allergensNames = new ArrayList<>();
        allergensIds = new ArrayList<>();
        customTagsNames = new ArrayList<>();
        customTagsIds = new ArrayList<>();
    }

    public RecipeDTO(Recipe recipe) {
        this.id = recipe.getId();
        if (recipe.getCommonRecipeId() != null)
            this.commonRecipeId = recipe.getCommonRecipeId().getId();
        this.name = recipe.getName();
        this.username = recipe.getUser().getUsername();
        this.mealCategoryName = recipe.getMealCategory().getName();
        this.mealCategoryID = recipe.getMealCategory().getId();
        this.regionalCuisineName = recipe.getRegionalCuisine() != null ? recipe.getRegionalCuisine().getName() : "";
        this.regionalCuisineID = recipe.getRegionalCuisine() != null ? recipe.getRegionalCuisine().getId() : null;
        this.cookingTimeMin = recipe.getCookingTimeMin();
        this.portions = recipe.getPortions();
        this.calories = recipe.getCalories();
        dishesByIngredientsNames = new ArrayList<>();
        dishesByIngredientsIds = new ArrayList<>();
        allergensNames = new ArrayList<>();
        allergensIds = new ArrayList<>();
        customTagsNames = new ArrayList<>();
        customTagsIds = new ArrayList<>();
    }

    public void setRecipeText(String text) {
        if (text != null) {
            this.recipeTextLine = text;
            this.recipeText = text.split("\n");
        } else {
            this.recipeTextLine = "";
            this.recipeText = new String[]{""};
        }
    }


}
