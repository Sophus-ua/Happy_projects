package persistence.dao.services.interfaces;



import exceptions.DatabaseUpdateException;
import exceptions.EmptyRecipeIdsException;
import org.springframework.stereotype.Service;
import persistence.entity.*;

import java.util.List;

@Service
public interface IRecipeService {

    String findNameById(long id);
    Recipe findById(long id);
    byte[] getImageDataById(long id);
    List<Long>findRecipeIdsByNameLikeForUser(String nameLike, String username) throws EmptyRecipeIdsException;
    void updateImageDataById(byte[] imageData, Long recipeId);
    String deleteByIdAndUsername(long id, String username);

    Long copyRecipeToUserById(String username, Long recipeID) throws DatabaseUpdateException;

    boolean commonRecipeIsPresent(Long commonRecipeId, String username);
}
