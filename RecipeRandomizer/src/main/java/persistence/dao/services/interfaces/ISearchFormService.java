package persistence.dao.services.interfaces;


import exceptions.EmptyRecipeIdsException;
import models.SearchModel;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import persistence.entity.Recipe;
import java.util.List;



@Service
public interface ISearchFormService {
    @NonNull
    Page<Recipe> findPageOfRecipesByIds(List<Long> recipesIds, int page, int size) throws EmptyRecipeIdsException;
    @NonNull
    List<Long> findRecipeIdsForUserBySearchModel(SearchModel searchModel, String username) throws EmptyRecipeIdsException;
    @NonNull
    String getRandomizeRecipeIdsJSON(SearchModel searchModel, String username) throws EmptyRecipeIdsException ;

}
