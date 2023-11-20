package persistence.dao.services.interfaces;

import models.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDTOService {
    List<MealCategoryDTO>  findAllMealCategories();
    List<RegionalCuisineDTO> findAllRegionalCuisines();
    List<DishByIngredientsDTO> findAllDishesByIngredients();
    List<AllergenDTO> findAllAllergens();
    List<CustomTagDTO> findAllCustomTagsForUser(String username);
    List<UserDTO> findAllUsers();
    RecipeDTO findRecipeDTO(long id);
    RecipeDTO findRecipeDTOForUser(long commonRecipeId, String username);
}
