package persistence.dao.repositories;



import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.MealCategory;




@Repository
@Transactional
public interface IMealCategoryRepository extends CrudRepository<MealCategory, Long> {


    @Modifying
    @Query(value = "ALTER TABLE meal_categories AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
