package persistence.dao.services.interfaces;


import models.SearchModel;
import org.springframework.stereotype.Service;
import persistence.entity.Recipe;
import java.util.List;



@Service
public interface ISearchFormService {
    List<Recipe> findRecipesBySearchModel(SearchModel searchModel);
    String findAndRandomizeRecipeIdsJSON(SearchModel searchModel);
}
