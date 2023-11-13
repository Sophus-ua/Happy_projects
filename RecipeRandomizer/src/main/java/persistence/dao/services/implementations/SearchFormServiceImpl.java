package persistence.dao.services.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.EmptyRecipeIds;
import lombok.extern.slf4j.Slf4j;
import models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Recipe> findRecipesBySearchModel(SearchModel searchModel) {
        Set<Integer> recipesIds = findRecipesIdsBySearchModel(searchModel);
        Set<Long> longIds = recipesIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toSet());

        Iterable<Recipe> iterable = recipeRepository.findAllById(longIds);
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public String findAndRandomizeRecipeIdsJSON(SearchModel searchModel) {
        Set<Integer> recipesIds = findRecipesIdsBySearchModel(searchModel);
        List<Integer> idsList = new ArrayList<>(recipesIds);
        Collections.shuffle(idsList);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(idsList);
        } catch (JsonProcessingException e) {

            System.err.println("Error parsing recipesIds to Json in " +
                    "SearchFormServiceImpl.findRecipeIdsBySearchModelJSON()");
            throw new RuntimeException(e);
        }
    }


//    @NonNull
//    private Set<Integer> findRecipesIdsBySearchModel(SearchModel searchModel) {
//
//        Set<Integer> recipesIds = new HashSet<>();
//        List<Long> variableList = new ArrayList<>();
//
//        /*1*/
//        variableList.add(searchModel.getIncludeMealCategoryID());
//        if (variableList.get(0) != 0) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_MEAL_CATEGORY, recipesIds, variableList, true);
//
//            System.out.print("\nrecipesIds in 1: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//        /*2*/
//        variableList.clear();
//        variableList.add(searchModel.getIncludeRegionalCuisineID());
//        if (variableList.get(0) != 0) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_REGIONAL_CUISINE, recipesIds, variableList, true);
//
//            System.out.print("\nrecipesIds in 2: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//        /*3*/
//        variableList = searchModel.getIncludeDishesByIngredientsIds();
//        if (!variableList.isEmpty() && variableList.get(0) != 0 || variableList.size() > 1) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_DISHES_INGREDIENTS, recipesIds, variableList, true);
//
//            System.out.print("\nrecipesIds in 3: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//        /*4*/
//        variableList = searchModel.getIncludeCustomTagsIds();
//        if (!variableList.isEmpty() && variableList.get(0) != 0 || variableList.size() > 1) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_TAGS, recipesIds, variableList, true);
//
//            System.out.print("\nrecipesIds in 4: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//        /*5*/
//        variableList.clear();
//        variableList.add(searchModel.getMaxCookingTime());
//        recipesIds = filterRecipeIds(FilteringParameter.BY_COOKING_TIME, recipesIds, variableList, true);
//        System.out.print("\nrecipesIds in 5: ");
//        recipesIds.forEach(System.out::print);
//        if (recipesIds.isEmpty()) return recipesIds;
//        /*6*/
//        variableList = searchModel.getExcludeDishesByIngredientsIds();
//        if (!variableList.isEmpty() && variableList.get(0) != 0 || variableList.size() > 1) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_DISHES_INGREDIENTS, recipesIds, variableList, false);
//
//            System.out.print("\nrecipesIds in 6: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//        /*7*/
//        variableList = searchModel.getExcludeFoodWithAllergensIds();
//        if (!variableList.isEmpty() && variableList.get(0) != 0 || variableList.size() > 1) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_ALLERGENS, recipesIds, variableList, false);
//
//            System.out.print("\nrecipesIds in 7: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//        /*8*/
//        variableList = searchModel.getExcludeCustomTagsIds();
//        if (!variableList.isEmpty() && variableList.get(0) != 0 || variableList.size() > 1) {
//            recipesIds = filterRecipeIds(FilteringParameter.BY_TAGS, recipesIds, variableList, false);
//
//            System.out.print("\nrecipesIds in 8: ");
//            recipesIds.forEach(System.out::print);
//
//            if (recipesIds.isEmpty()) return recipesIds;
//        }
//
//        System.out.println("\n");
//        return recipesIds;
//    }





    @NonNull
    private Set<Integer> findRecipesIdsBySearchModel(SearchModel searchModel) {
        Set<Integer> recipesIds = new HashSet<>();
        List<Long> variableList = new ArrayList<>();
        Object[][] filteringParameters = getArrayOfFilteringParameters();
        int filteringOrder = 0;

        try {
            variableList.add(searchModel.getIncludeMealCategoryID());
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList.clear();
            variableList.add(searchModel.getIncludeRegionalCuisineID());
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList = searchModel.getIncludeDishesByIngredientsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList = searchModel.getIncludeCustomTagsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList.clear();
            variableList.add(searchModel.getMaxCookingTime());
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList = searchModel.getExcludeDishesByIngredientsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList = searchModel.getExcludeFoodWithAllergensIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder++);

            variableList = searchModel.getExcludeCustomTagsIds();
            recipesIds = checkingThenFilteringRecipeIds(recipesIds, variableList, filteringParameters, filteringOrder);

        } catch (EmptyRecipeIds e) {
            return new HashSet<>();
        }
        System.out.println("\n");
        return recipesIds;
    }

    @NonNull
    private Set<Integer> checkingThenFilteringRecipeIds(Set<Integer> recipesIds, List<Long> variableList,
                                                        Object[][] filteringParameters, int filteringOrder) throws EmptyRecipeIds {

        if (variableList.isEmpty() || variableList.get(0) == 0 && variableList.size() == 1)
            return recipesIds;

        recipesIds = filterRecipeIds((FilteringParameter)filteringParameters[0][filteringOrder], recipesIds,
                variableList, (Boolean) filteringParameters[1][filteringOrder]);

        System.out.print("\nSearchFormServiceImpl recipesIds in bloc " + (filteringOrder+1) + " : ");
        recipesIds.forEach(System.out::print);

        if (recipesIds.isEmpty()) throw new EmptyRecipeIds();
        return recipesIds;
    }

    @NonNull
    private Set<Integer> filterRecipeIds(FilteringParameter parameter,
                                         Set<Integer> recipesIds, List<Long> listIds, boolean isInclude) {

        Iterable<Integer> iterable = null;
        switch (parameter) {
            case BY_MEAL_CATEGORY:
                iterable = recipeRepository.findIdsByMealCategory_Id(listIds.get(0));
                break;
            case BY_REGIONAL_CUISINE:
                iterable = recipeRepository.findIdsByRegionalCuisine_Id(listIds.get(0));
                break;
            case BY_COOKING_TIME:
                iterable = recipeRepository.findIdsByCookingTimeLessThan(listIds.get(0));
                break;
            case BY_DISHES_INGREDIENTS:
                iterable = recipeRepository.findRecipeIdsByDishesByIngredientsIds(listIds);
                break;
            case BY_ALLERGENS:
                iterable = recipeRepository.findRecipeIdsByAllergensIds(listIds);
                break;
            case BY_TAGS:
                iterable = recipeRepository.findRecipeIdsByTagsIds(listIds);
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
