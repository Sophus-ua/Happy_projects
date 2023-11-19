package persistence.dao.services.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.EmptyRecipeIdsException;
import lombok.extern.slf4j.Slf4j;
import models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.IRecipeRepository;
import persistence.dao.services.interfaces.ISearchFormService;
import persistence.entity.Recipe;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class SearchFormServiceImpl implements ISearchFormService {

    private IRecipeRepository recipeRepository;

    @Override
    @NonNull
    public Page<Recipe> findPageOfRecipesByIds(List<Long> recipesIds, int page, int size) throws EmptyRecipeIdsException {
        if (recipesIds == null || page ==0 || size == 0)
            throw new EmptyRecipeIdsException("Список пошуку пустий :(");

        PageRequest pageable = PageRequest.of(page - 1, size);
        return recipeRepository.findAllPagesById(recipesIds, pageable);
    }

    @Override
    @NonNull
    public String getRandomizeRecipeIdsJSON(SearchModel searchModel, String username) {
        List<Long> recipeIds = findRecipeIdsForUserBySearchModel(searchModel,username);
        Collections.shuffle(recipeIds);
        recipeIds = recipeIds.subList(0, Math.min(12, recipeIds.size()));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(recipeIds);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing recipesIds to Json in " +
                    "SearchFormServiceImpl.findRecipeIdsBySearchModelJSON()");
            throw new RuntimeException(e);
        }
    }

    @Override
    @NonNull
    public List<Long> findRecipeIdsForUserBySearchModel(SearchModel searchModel, String username) {
        Set<Integer> recipesIds = new HashSet<>();
        List<Long> variableList = new ArrayList<>();
        Object[][] filteringParameters = getArrayOfFilteringParameters();
        int filteringOrder = 0;

        try {
            variableList.add(searchModel.getIncludeMealCategoryID());
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList.clear();
            variableList.add(searchModel.getIncludeRegionalCuisineID());
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList = searchModel.getIncludeDishesByIngredientsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList = searchModel.getIncludeCustomTagsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList.clear();
            variableList.add(searchModel.getMaxCookingTime());
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList = searchModel.getExcludeDishesByIngredientsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList = searchModel.getExcludeFoodWithAllergensIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder++, username);

            variableList = searchModel.getExcludeCustomTagsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters,
                    filteringOrder, username);

        } catch (EmptyRecipeIdsException e) {
            return new ArrayList<>();
        }
        System.out.println("\n");
        return recipesIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
    }

    @NonNull
    private Set<Integer> checkingThenFilteringRecipeIds(Set<Integer> recipesIds, List<Long> variableList,
                                                        Object[][] filteringParameters, int filteringOrder,
                                                        String username) throws EmptyRecipeIdsException {

        if (variableList.isEmpty() || variableList.get(0) == 0 && variableList.size() == 1)
            return recipesIds;

        recipesIds = filterRecipeIds((FilteringParameter)filteringParameters[0][filteringOrder], recipesIds,
                variableList, (Boolean) filteringParameters[1][filteringOrder], username);

        System.out.print("\nSearchFormServiceImpl recipesIds in bloc " + (filteringOrder+1) + " : ");
        recipesIds.forEach(System.out::print);

        if (recipesIds.isEmpty()) throw new EmptyRecipeIdsException();
        return recipesIds;
    }

    @NonNull
    private Set<Integer> filterRecipeIds(FilteringParameter parameter,
                                         Set<Integer> recipesIds, List<Long> listIds,
                                         boolean isInclude, String username) {

        Iterable<Integer> iterable = null;
        switch (parameter) {
            case BY_MEAL_CATEGORY:
                iterable = recipeRepository.findIdsForUserByMealCategoryId(listIds.get(0), username);
                break;
            case BY_REGIONAL_CUISINE:
                iterable = recipeRepository.findIdsForUserByRegionalCuisineId(listIds.get(0), username);
                break;
            case BY_COOKING_TIME:
                iterable = recipeRepository.findIdsForUserByCookingTimeLessThan(listIds.get(0), username);
                break;
            case BY_DISHES_INGREDIENTS:
                iterable = recipeRepository.findRecipeIdsForUserByDishesByIngredientsIds(listIds, username);
                break;
            case BY_ALLERGENS:
                iterable = recipeRepository.findRecipeIdsForUserByAllergensIds(listIds, username);
                break;
            case BY_TAGS:
                iterable = recipeRepository.findRecipeIdsForUserByTagsIds(listIds, username);
                break;
        }
        Set<Integer> filteredRecipesIds = StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toSet());

        if (isInclude && !recipesIds.isEmpty())
            filteredRecipesIds.retainAll(recipesIds);
        if (!isInclude) {
            Set<Integer> set = new HashSet<>(recipesIds);
            set.removeAll(filteredRecipesIds);
            filteredRecipesIds = set;
        }
        return filteredRecipesIds;
    }

    @NonNull
    private Object[][] getArrayOfFilteringParameters() {
        Object[][] mixedMatrix = new Object[2][8];
        mixedMatrix[0][0] = FilteringParameter.BY_MEAL_CATEGORY;
        mixedMatrix[0][1] = FilteringParameter.BY_REGIONAL_CUISINE;
        mixedMatrix[0][2] = FilteringParameter.BY_DISHES_INGREDIENTS;
        mixedMatrix[0][3] = FilteringParameter.BY_TAGS;
        mixedMatrix[0][4] = FilteringParameter.BY_COOKING_TIME;
        mixedMatrix[0][5] = FilteringParameter.BY_DISHES_INGREDIENTS;
        mixedMatrix[0][6] = FilteringParameter.BY_ALLERGENS;
        mixedMatrix[0][7] = FilteringParameter.BY_TAGS;

        mixedMatrix[1][0] = true;
        mixedMatrix[1][1] = true;
        mixedMatrix[1][2] = true;
        mixedMatrix[1][3] = true;
        mixedMatrix[1][4] = true;
        mixedMatrix[1][5] = false;
        mixedMatrix[1][6] = false;
        mixedMatrix[1][7] = false;

        return mixedMatrix;
    }

    private enum FilteringParameter {
        BY_MEAL_CATEGORY, BY_REGIONAL_CUISINE, BY_COOKING_TIME,
        BY_DISHES_INGREDIENTS, BY_ALLERGENS, BY_TAGS
    }

    @Autowired
    public void setRecipeRepository(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
