package persistence.dao.services.implementations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.IRecipeRepository;
import persistence.dao.services.interfaces.IRecipeService;
import persistence.entity.Recipe;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class RecipeServiceImpl implements IRecipeService {
    private IRecipeRepository recipeRepository;

    @Override
    public void test(long id) {

    }

    @Override
    public void updateImageDataById(byte[] imageData, Long recipeId) {
        recipeRepository.updateImageDataById(imageData,recipeId);
    }

    @Override
    @NonNull
    public String findNameById(long id) {
        Optional<String> optional = recipeRepository.findNameById(id);
        return optional.orElse("");
    }

    @Override
    public byte[] getImageDataById(long id) {
        return recipeRepository.findImageDataById(id);
    }

    @Override
    public Recipe findById(long id) {
        Recipe recipe = null;
        Optional<Recipe> optional = recipeRepository.findById(id);
        if (optional.isPresent())
            recipe = optional.get();
        return recipe;
    }

    @Override
    public List<Recipe> findRecipesByNameLikeForUser(String nameLike, String username) {
        Iterable<Recipe> iterable = recipeRepository.findByNameLikeForUser("%" + nameLike + "%", username);
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public String deleteByIdAndUsername(long recipeID, String username){
        recipeRepository.deleteByIdAndUsername(recipeID, username);
        recipeRepository.resetAutoIncrement();

        boolean exists = recipeRepository.existsById(recipeID);
        if (!exists)
            return Messages.RECIPE_DELETED.message;
        else
            return Messages.FAILED_TO_DELETE_RECIPE.message;
    }

    private enum Messages {
        RECIPE_DELETED("Рецепт був успішно видаленний "),
        FAILED_TO_DELETE_RECIPE("Не вдалося видалити рецепт :( ");

        final String message;

        Messages(String message) {
            this.message = message;
        }
    }

    @Autowired
    public void setAllergenRepository(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
