package persistence.dao.services.implementations;


import lombok.extern.slf4j.Slf4j;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.*;
import persistence.dao.services.interfaces.IDTOService;

import persistence.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class DTOServiceImpl implements IDTOService {
    private final IMealCategoryRepository mealCategoryRepository;
    private final IRegionalCuisineRepository regionalCuisineRepository;
    private final IDishByIngredientsRepository dishByIngredientsRepository;
    private final IAllergenRepository allergenRepository;
    private final ICustomTagRepository customTagRepository;
    private final IRecipeRepository recipeRepository;
    private final IUserRepository userRepository;

    @Autowired
    public DTOServiceImpl(IMealCategoryRepository mealCategoryRepository, IAllergenRepository allergenRepository,
                          IRegionalCuisineRepository regionalCuisineRepository, IRecipeRepository recipeRepository,
                          ICustomTagRepository customTagRepository, IUserRepository userRepository,
                          IDishByIngredientsRepository dishByIngredientsRepository) {
        this.mealCategoryRepository = mealCategoryRepository;
        this.regionalCuisineRepository = regionalCuisineRepository;
        this.dishByIngredientsRepository = dishByIngredientsRepository;
        this.allergenRepository = allergenRepository;
        this.customTagRepository = customTagRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<MealCategoryDTO> findAllMealCategories() {
        Iterable<MealCategory> iterable = mealCategoryRepository.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(MealCategoryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionalCuisineDTO> findAllRegionalCuisines() {
        Iterable<RegionalCuisine> iterable = regionalCuisineRepository.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(RegionalCuisineDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishByIngredientsDTO> findAllDishesByIngredients() {
        Iterable<DishByIngredients> iterable = dishByIngredientsRepository.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(DishByIngredientsDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AllergenDTO> findAllAllergens() {
        Iterable<Allergen> iterable = allergenRepository.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(AllergenDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomTagDTO> findAllCustomTagsForUser(String username) {
        Iterable<CustomTag> iterable = customTagRepository.findAllForUser(username);
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(CustomTagDTO::new)
                .collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.username == authentication.name || returnObject.username == \"Moderator\"")
    @Nullable
    @Override
    public RecipeDTO findRecipeDTOForUser(long commonRecipeId, String username) {
        List<Integer> recipeIds = null;
        if (!(username.equalsIgnoreCase("Moderator")))
            recipeIds = recipeRepository.getIdByCommonIdForUser(commonRecipeId, username);

        if (recipeIds != null && !recipeIds.isEmpty())
            return findRecipeDTO(recipeIds.get(0));
        else
            return findRecipeDTO(commonRecipeId);

    }

    @PostAuthorize("returnObject.username == authentication.name || returnObject.username == \"Moderator\"")
    @Nullable
    @Override
    public RecipeDTO findRecipeDTO(long id) {
        Optional<Recipe> optional = recipeRepository.findById(id);
        if (!optional.isPresent())
            return null;

        RecipeDTO recipeDTO = new RecipeDTO(optional.get());

        String recipeText = recipeRepository.findRecipeTextById(id);
        recipeDTO.setRecipeText(recipeText);

        List<String> list = dishByIngredientsRepository.findNamesByRecipeId(id);
        recipeDTO.setDishesByIngredientsNames(list);
        List<Integer> intListIds = dishByIngredientsRepository.findIdsByRecipeId(id);
        List<Long> longListIds = intListIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
        recipeDTO.setDishesByIngredientsIds(longListIds);


        list = allergenRepository.findNamesByRecipeId(id);
        recipeDTO.setAllergensNames(list);
        intListIds = allergenRepository.findIdsByRecipeId(id);
        longListIds = intListIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
        recipeDTO.setAllergensIds(longListIds);


        list = customTagRepository.findNamesByRecipeId(id);
        recipeDTO.setCustomTagsNames(list);
        intListIds = customTagRepository.findIdsByRecipeId(id);
        longListIds = intListIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
        recipeDTO.setCustomTagsIds(longListIds);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DTOServiceImpl.findRecipeDTO authentication.getName(): " + authentication.getName());
        System.out.println("DTOServiceImpl.findRecipeDTO recipeDTO: " + recipeDTO);
        return recipeDTO;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        Iterable<User> iterable = userRepository.findAllUsers();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
}
