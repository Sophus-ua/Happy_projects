package persistence.dao.services.implementations;


import exceptions.DatabaseUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.dao.repositories.IRecipeRepository;
import persistence.dao.repositories.IUserRepository;
import persistence.dao.services.interfaces.IRecipeService;
import persistence.entity.Allergen;
import persistence.entity.DishByIngredients;
import persistence.entity.Recipe;
import persistence.entity.User;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class RecipeServiceImpl implements IRecipeService {
    private IRecipeRepository recipeRepository;

    private IUserRepository userRepository;


    @Override
    public void updateImageDataById(byte[] imageData, Long recipeId) {
        recipeRepository.updateImageDataById(imageData, recipeId);
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
    public List<Long> findRecipeIdsByNameLikeForUser(String nameLike, String username) {
        Iterable<Integer> iterable = recipeRepository.findIdsByNameLikeForUser("%" + nameLike + "%", username);

        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(Integer::longValue)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public String deleteByIdAndUsername(long recipeID, String username) {
        recipeRepository.deleteByIdAndUsername(recipeID, username);
        recipeRepository.resetAutoIncrement();

        boolean exists = recipeRepository.existsById(recipeID);
        if (!exists)
            return Messages.RECIPE_DELETED.message;
        else
            return Messages.FAILED_TO_DELETE_RECIPE.message;
    }

    @Transactional
    @Override
    @NonNull
    public Long copyRecipeToUserById(String username, Long recipeID) throws DatabaseUpdateException {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeID);
        if (!recipeOptional.isPresent())
            throw new DatabaseUpdateException(Messages.FAILED_TO_FIND_RECIPE.message);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent())
            throw new DatabaseUpdateException(Messages.FAILED_TO_FIND_USER.message);

        Recipe commonRecipe = recipeOptional.get();

        Recipe userNewRecipe = new Recipe(
                commonRecipe.getName(), commonRecipe.getMealCategory(),
                commonRecipe.getRecipeText(), userOptional.get()
        );

        userNewRecipe.setRegionalCuisine(commonRecipe.getRegionalCuisine());
        userNewRecipe.setCookingTimeMin(commonRecipe.getCookingTimeMin());
        userNewRecipe.setPortions(commonRecipe.getPortions());
        userNewRecipe.setCalories(commonRecipe.getCalories());
        userNewRecipe.setImageData(commonRecipe.getImageData());
        if (commonRecipe.getDishesByIngredients() != null) {
            for (DishByIngredients d : commonRecipe.getDishesByIngredients())
                userNewRecipe.addDishByIngredients(d);
        }
        if (commonRecipe.getAllergens() != null) {
            for (Allergen a : commonRecipe.getAllergens())
                userNewRecipe.addAllergen(a);
        }

        userNewRecipe = recipeRepository.save(userNewRecipe);

        return userNewRecipe.getId();
    }


    private enum Messages {
        RECIPE_DELETED("Рецепт був успішно видаленний "),
        FAILED_TO_DELETE_RECIPE("Не вдалося видалити рецепт :( "),
        FAILED_TO_FIND_RECIPE("Не вдалося знайти рецепт :( "),
        FAILED_TO_FIND_USER("Не вдалося знайти користувача :( ");

        final String message;

        Messages(String message) {
            this.message = message;
        }
    }

    @Autowired
    public void setAllergenRepository(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
