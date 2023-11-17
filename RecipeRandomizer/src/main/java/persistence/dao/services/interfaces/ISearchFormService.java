package persistence.dao.services.interfaces;


import models.SearchModel;
import org.springframework.stereotype.Service;
import persistence.entity.Recipe;
import java.util.List;



@Service
public interface ISearchFormService {
    List<Recipe> findRecipesForUserBySearchModel(SearchModel searchModel, String username);
    String findAndRandomizeRecipeIdsForUserJSON(SearchModel searchModel, String username);
}
