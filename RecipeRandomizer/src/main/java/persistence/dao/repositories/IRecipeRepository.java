package persistence.dao.repositories;



import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.Recipe;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface IRecipeRepository extends CrudRepository<Recipe, Long> {
    @Query(value = "select name from recipes where id = ?1", nativeQuery = true)
    Optional<String> findNameById(long id);

    @Modifying
    @Query(value = "update recipes\n" +
            "set image_data = ?1 where id = ?2", nativeQuery = true)
    void updateImageDataById(byte[] imageData, Long recipeId);

    @Modifying
    @Query(value = "update recipes\n" +
            "set recipe_text = ?1 where id = ?2", nativeQuery = true)
    void updateRecipeTextById(String recipeText, Long recipeId);

    @Query(value = "select recipe_text from recipes where id = ?1", nativeQuery = true)
    String findRecipeTextById(Long recipeId);

    @Query(value = "select image_data from recipes where id = ?1", nativeQuery = true)
    byte[] findImageDataById(Long recipeId);

    @Query(value = "select id from recipes", nativeQuery = true)
    Iterable<Integer> findAllRecipesIds();
    @Query(value = "select * from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "where r.name like ?1 and u.username = ?2", nativeQuery = true)
    Iterable<Recipe> findByNameLikeForUser(String nameLike, String username);

    @Query(value = "select r.id from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "where r.meal_category_id = ?1 and u.username = ?2", nativeQuery = true)
    Iterable<Integer> findIdsForUserByMealCategoryId(long mealCategoryId, String username);

    @Query(value = "select r.id from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "where r.regional_cuisine_id = ?1 and u.username = ?2", nativeQuery = true)
    Iterable<Integer> findIdsForUserByRegionalCuisineId(long regionalCuisineId, String username);

    @Query(value = "select r.id from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "where (cooking_time <= ?1 or cooking_time is null) and u.username = ?2", nativeQuery = true)
    Iterable<Integer> findIdsForUserByCookingTimeLessThan(long cookingTime, String username);


    @Query(value = "select r.id from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "join recipes_dishes_by_ingredients di on di.recipe_id = r.id\n" +
            "where di.dish_by_ingredients_id in :dishesByIngredientsIds and u.username = :username", nativeQuery = true)
    Iterable<Integer> findRecipeIdsForUserByDishesByIngredientsIds(
            @Param("dishesByIngredientsIds") List<Long> dishesByIngredientsIds, @Param("username") String username);


    @Query(value = "select r.id from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "join recipes_common_allergens a on a.recipe_id = r.id\n" +
            "where a.allergen_id in :allergensIds and u.username = :username", nativeQuery = true)
    Iterable<Integer> findRecipeIdsForUserByAllergensIds(
            @Param("allergensIds") List<Long> allergensIds, @Param("username") String username);


    @Query(value = "select r.id from recipes r\n" +
            "join users u on u.id = r.user_id\n" +
            "join recipes_custom_tags t on t.recipe_id = r.id\n" +
            "where t.custom_tag_id in :tagsIds and u.username = :username", nativeQuery = true)
    Iterable<Integer> findRecipeIdsForUserByTagsIds(
            @Param("tagsIds") List<Long> tagsIds, @Param("username") String username);

    @Modifying
    @Query(value = "ALTER TABLE recipes AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
