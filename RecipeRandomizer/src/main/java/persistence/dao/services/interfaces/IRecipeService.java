package persistence.dao.services.interfaces;



import org.springframework.stereotype.Service;
import persistence.entity.*;

import java.util.List;

@Service
public interface IRecipeService {

    String findNameById(long id);
    void test(long id);
    Recipe findById(long id);

    byte[] getImageDataById(long id);

    List<Recipe> findRecipesByNameLike(String recipeName);

    void updateImageDataById(byte[] imageData, Long recipeId);

    String deleteById(long id);
}
