package persistence.dao.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.User;

import java.util.Optional;

@Repository
@Transactional
public interface IUserRepository  extends CrudRepository<User, Long> {

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "select id from users where username = ?1", nativeQuery = true)
    Integer getIdByUsername(String username);

    @Modifying
    @Query(value = "ALTER TABLE users AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
