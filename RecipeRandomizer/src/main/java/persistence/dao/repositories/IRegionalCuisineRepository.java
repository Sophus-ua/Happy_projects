package persistence.dao.repositories;




import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import persistence.entity.RegionalCuisine;




@Repository
@Transactional
public interface IRegionalCuisineRepository extends CrudRepository<RegionalCuisine, Long> {

}
