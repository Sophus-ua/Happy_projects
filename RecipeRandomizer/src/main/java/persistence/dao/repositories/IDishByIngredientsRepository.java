package persistence.dao.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.DishByIngredients;

import java.util.List;


@Repository
@Transactional
public interface IDishByIngredientsRepository extends CrudRepository<DishByIngredients, Long> {
    @Query(value = "select d.name from dishes_by_ingredients d\n" +
            "join recipes_dishes_by_ingredients rd on rd.dish_by_ingredients_id = d.id\n" +
            "where rd.recipe_id = ?1", nativeQuery = true)
    List<String> findNamesByRecipeId (long recipeId);

    @Query(value = "select dish_by_ingredients_id from recipes_dishes_by_ingredients\n" +
            "where recipe_id = ?1", nativeQuery = true)
    List<Integer> findIdsByRecipeId (long recipeId);

    @Modifying
    @Query(value = "ALTER TABLE dishes_by_ingredients AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
