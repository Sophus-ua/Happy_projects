package persistence.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import persistence.entity.ImageBuffer;

@Repository
@Transactional
public interface IImageBufferRepository extends CrudRepository<ImageBuffer, String> {
}
