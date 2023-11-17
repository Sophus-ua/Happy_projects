package persistence.dao.repositories;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.Allergen;

import java.util.List;


@Repository
@Transactional
public interface IAllergenRepository extends CrudRepository<Allergen, Long> {

    @Query(value = "select ca.name from common_allergens ca\n" +
            "join recipes_common_allergens ra on ra.allergen_id = ca.id\n" +
            "where ra.recipe_id = ?1", nativeQuery = true)
    List<String> findNamesByRecipeId (long recipeId);

    @Query(value = "select allergen_id from recipes_common_allergens\n" +
            "where recipe_id = ?1", nativeQuery = true)
    List<Integer> findIdsByRecipeId (long recipeId);

}
