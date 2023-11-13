package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SearchModel {
    private long includeMealCategoryID;
    private long includeRegionalCuisineID;
    private long maxCookingTime;
    private List<Long> includeDishesByIngredientsIds;
    private List<Long> excludeDishesByIngredientsIds;
    private List<Long> excludeFoodWithAllergensIds;
    private List<Long> includeCustomTagsIds;
    private List<Long> excludeCustomTagsIds;

    public SearchModel(){
        includeDishesByIngredientsIds = new ArrayList<>();
        excludeDishesByIngredientsIds = new ArrayList<>();
        excludeFoodWithAllergensIds = new ArrayList<>();
        includeCustomTagsIds = new ArrayList<>();
        excludeCustomTagsIds = new ArrayList<>();
    }
}
