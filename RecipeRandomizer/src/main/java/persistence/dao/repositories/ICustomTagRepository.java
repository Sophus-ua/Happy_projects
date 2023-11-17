package persistence.dao.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.CustomTag;

import java.util.List;


@Repository
@Transactional
public interface ICustomTagRepository extends CrudRepository<CustomTag, Long> {
    @Query(value = "select * from custom_tags ct\n" +
            "join users u on u.id = ct.user_id\n" +
            "where u.username = ?1", nativeQuery = true)
    Iterable<CustomTag> findAllForUser(String username);

    @Query(value = "select ct.name from custom_tags ct\n" +
            "join recipes_custom_tags rt on rt.custom_tag_id = ct.id\n" +
            "where rt.recipe_id = ?1", nativeQuery = true)
    List<String> findNamesByRecipeId (long recipeId);

    @Query(value = "select custom_tag_id from recipes_custom_tags\n" +
            "where recipe_id = ?1", nativeQuery = true)
    List<Integer> findIdsByRecipeId (long recipeId);

    @Modifying
    @Query(value = "ALTER TABLE custom_tags AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
