package persistence.dao.services.interfaces;

import models.RecipeDTO;
import org.springframework.stereotype.Service;

@Service
public interface IRecipeDesignerService {
    String parseRecipeDTOtoJson (RecipeDTO recipeDTO);

    RecipeDTO saveRecipeByRecipeDTO (RecipeDTO recipeDTO);
}
