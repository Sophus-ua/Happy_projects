package persistence.dao.services.interfaces;

import models.ImageDTO;
import models.RecipeDTO;
import org.springframework.stereotype.Service;


@Service
public interface IRecipeImageCacheService {
    byte[] saveImageToCacheWithEvictionDelay(ImageDTO imageDTO);
    void saveImageToBufferWithEvictionDelay(ImageDTO imageDTO);

    String updateImageFromCacheWithDelay(RecipeDTO recipeDTO);
    String updateImageFromBufferWithDelay(RecipeDTO recipeDTO);

}
