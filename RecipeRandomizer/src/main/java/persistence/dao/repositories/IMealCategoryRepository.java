package persistence.dao.repositories;




import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.MealCategory;




@Repository
@Transactional
public interface IMealCategoryRepository extends CrudRepository<MealCategory, Long> {
}
