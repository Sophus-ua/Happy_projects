package persistence.dao.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import models.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.dao.repositories.*;
import persistence.dao.services.interfaces.IRecipeDesignerService;
import persistence.entity.*;

import java.util.*;



@Slf4j
@Service
public class RecipeDesignerServiceImpl implements IRecipeDesignerService {

    private IMealCategoryRepository mealCategoryRepository;
    private IRegionalCuisineRepository regionalCuisineRepository;
    private IDishByIngredientsRepository dishByIngredientsRepository;
    private IAllergenRepository allergenRepository;
    private ICustomTagRepository customTagRepository;
    private IRecipeRepository recipeRepository;

    @Nullable
    @Override
    public String parseRecipeDTOtoJson(RecipeDTO recipeDTO) {
        String recipeDTOJson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            recipeDTOJson = objectMapper.writeValueAsString(recipeDTO);
        } catch (JsonProcessingException e) {
            System.err.println("Error in DTOServiceImpl.findRecipeDTOJson");
        }
        return recipeDTOJson;
    }

    @Transactional
    @Override
    @NonNull
    public RecipeDTO saveRecipeByRecipeDTO(RecipeDTO recipeDTO) {
        System.out.println("recipeDTO in DTOServiceImpl.saveRecipeByRecipeDTO: \n" + recipeDTO);

        Recipe recipe = new Recipe();

        if (recipeDTO.getId() != null) {
            Optional<Recipe> optional = recipeRepository.findById(recipeDTO.getId());
            if (optional.isPresent()) recipe = optional.get();
        }

        recipe.setName(recipeDTO.getName());

        Optional<MealCategory> optionalM = mealCategoryRepository.findById(recipeDTO.getMealCategoryID());
        if (optionalM.isPresent())
            recipe.setMealCategory(optionalM.get());

        Long regionalCuisineId = recipeDTO.getRegionalCuisineID();
        if (regionalCuisineId != null) {
            Optional<RegionalCuisine> optionalR = regionalCuisineRepository.findById(regionalCuisineId);
            if (optionalR.isPresent())
                recipe.setRegionalCuisine(optionalR.get());
        }

        recipe.setCookingTimeMin(recipeDTO.getCookingTimeMin());
        recipe.setPortions(recipeDTO.getPortions());
        recipe.setCalories(recipeDTO.getCalories());
        recipe.setRecipeText(recipeDTO.getRecipeTextLine());


        if (recipeDTO.getDishesByIngredientsIds() != null && !recipeDTO.getDishesByIngredientsIds().isEmpty()) {
            updateDishesByIngredients(recipe, recipeDTO.getDishesByIngredientsIds());
        }
        if (recipeDTO.getAllergensIds() != null && !recipeDTO.getAllergensIds().isEmpty()) {
            updateAllergens(recipe, recipeDTO.getAllergensIds());
        }
        if (recipeDTO.getCustomTagsIds() != null && !recipeDTO.getCustomTagsIds().isEmpty()) {
            updateCustomTags(recipe, recipeDTO.getCustomTagsIds());
        }


        String message;
        try {
            recipe = recipeRepository.save(recipe);
            System.out.println("Recipe після збереження : \n" + recipe);
        } catch (Exception e) {
            if (recipeDTO.getId() == null)
                message = ResultOfUpdateOrAddRecipe.NO_RECIPE_ADDED.message;
            else
                message = ResultOfUpdateOrAddRecipe.FAILED_TO_UPDATE.message;
//            return message;
        }
        if (recipeDTO.getId() == null)
            message = ResultOfUpdateOrAddRecipe.SUCCESSFULLY_ADDED.message;
        else
            message = ResultOfUpdateOrAddRecipe.SUCCESSFULLY_UPDATED.message;

        recipeDTO.setId(recipe.getId());
        return recipeDTO;
//        return message;
    }

    @Transactional
    private void updateDishesByIngredients(Recipe recipe, List<Long> dishesByIngredientsIds) {
        List<DishByIngredients> list = new ArrayList<>(recipe.getDishesByIngredients());
        for (DishByIngredients d : list) {
            if (dishesByIngredientsIds.contains(d.getId()))
                dishesByIngredientsIds.remove(d.getId());
            else
                recipe.removeDishByIngredientsById(d.getId());
        }
        Iterable<DishByIngredients> iterable = dishByIngredientsRepository.findAllById(dishesByIngredientsIds);
        for (DishByIngredients d : iterable) {
            recipe.addDishByIngredients(d);
        }
    }
    @Transactional

    private void updateAllergens(Recipe recipe, List<Long> allergensIds) {
        List<Allergen> list = new ArrayList<>(recipe.getAllergens());
        for (Allergen a : list) {
            if (allergensIds.contains(a.getId()))
                allergensIds.remove(a.getId());
            else
                recipe.removeAllergenById(a.getId());
        }
        Iterable<Allergen> iterable = allergenRepository.findAllById(allergensIds);
        for (Allergen a : iterable) {
            recipe.addAllergen(a);
        }
    }

    @Transactional
    private void updateCustomTags(Recipe recipe, List<Long> customTagsIds) {
        List<CustomTag> list = new ArrayList<>(recipe.getCustomTags());
        for (CustomTag c : list) {
            if (customTagsIds.contains(c.getId()))
                customTagsIds.remove(c.getId());
            else
                recipe.removeAllergenById(c.getId());
        }
        Iterable<CustomTag> iterable = customTagRepository.findAllById(customTagsIds);
        for (CustomTag c : iterable) {
            recipe.addCustomTag(c);
        }
    }

    private enum ResultOfUpdateOrAddRecipe {
        SUCCESSFULLY_ADDED("Рецепт успішно додано"),
        SUCCESSFULLY_UPDATED("Рецепт успішно оновлено"),
        NO_RECIPE_ADDED("Рецепт не вдалось додати :("),
        FAILED_TO_UPDATE("Рецепт не вдалось оновити :(");
        final String message;

        ResultOfUpdateOrAddRecipe(String message) {
            this.message = message;
        }
    }

    @Autowired
    public void setMealCategoryRepository(IMealCategoryRepository mealCategoryRepository) {
        this.mealCategoryRepository = mealCategoryRepository;
    }

    @Autowired
    public void setRegionalCuisineRepository(IRegionalCuisineRepository regionalCuisineRepository) {
        this.regionalCuisineRepository = regionalCuisineRepository;
    }

    @Autowired
    public void setDishByIngredientsRepository(IDishByIngredientsRepository dishByIngredientsRepository) {
        this.dishByIngredientsRepository = dishByIngredientsRepository;
    }

    @Autowired
    public void setAllergenRepository(IAllergenRepository allergenRepository) {
        this.allergenRepository = allergenRepository;
    }

    @Autowired
    public void setCustomTagRepository(ICustomTagRepository customTagRepository) {
        this.customTagRepository = customTagRepository;
    }

    @Autowired
    public void setRecipeRepository(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
