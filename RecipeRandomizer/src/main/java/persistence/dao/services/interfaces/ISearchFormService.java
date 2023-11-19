package persistence.dao.services.interfaces;


import models.SearchModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import persistence.entity.Recipe;

import java.util.List;
import java.util.Set;


@Service
public interface ISearchFormService {
    Page<Recipe> findPageOfRecipesByIds(List<Long> recipesIds, int page, int size);
    List<Long> findRecipeIdsForUserBySearchModel(SearchModel searchModel, String username);
    String getRandomizeRecipeIdsJSON(SearchModel searchModel, String username);

}
