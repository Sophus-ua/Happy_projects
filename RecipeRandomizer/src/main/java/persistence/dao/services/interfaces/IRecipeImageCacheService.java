package persistence.dao.services.interfaces;

import exceptions.ImageBufferException;
import models.ImageBufferDTO;
import models.RecipeDTO;
import org.springframework.stereotype.Service;


@Service
public interface IRecipeImageCacheService {

    void saveImageToBufferWithEvictionDelay(ImageBufferDTO imageBufferDTO) throws ImageBufferException;
    String updateImageFromBufferWithDelay(ImageBufferDTO imageDTO);

    byte[] saveImageToCacheWithEvictionDelay(ImageBufferDTO imageBufferDTO);
    String updateImageFromCacheWithDelay(RecipeDTO recipeDTO);
}
