package persistence.dao.repositories;



import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import persistence.entity.RegionalCuisine;




@Repository
@Transactional
public interface IRegionalCuisineRepository extends CrudRepository<RegionalCuisine, Long> {


    @Modifying
    @Query(value = "ALTER TABLE meal_categories AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
