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
    List<CustomTagDTO> findAllCustomTags();
    RecipeDTO findRecipeDTO(long id);


}
