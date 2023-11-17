package persistence.dao.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import persistence.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Transactional
public interface IUserRepository  extends CrudRepository<User, Long> {

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);


    boolean existsByUsername(String username);

    @Query(value = "select count(*) > 0 from users where id = ?1 and role = 'USER'", nativeQuery = true)
    Integer userExistsById(long id);

    @Query(value = "select id from users where username = ?1", nativeQuery = true)
    Integer getIdByUsername(String username);
    @Query(value = "select * from users where role = 'USER'", nativeQuery = true)
    Iterable<User> findAllUsers();

    @Query(value = "select enabled from users where role = 'USER' and id = ?1", nativeQuery = true)
    boolean isEnabled(long userId);

    @Modifying
    @Query(value = "update users\n" +
            "set enabled = CASE when enabled = 1 then 0 else 1 END\n" +
            "where id = ?1 and role = 'USER'", nativeQuery = true)
    void changeUserActivityStatus(long Id);

    @Modifying
    @Query(value = "update users\n" +
            "set last_login_date = ?1 where username = ?2", nativeQuery = true)
    void updateLastLoginDateByUsername(LocalDate loginDate, String username);

    @Modifying
    @Query(value = "ALTER TABLE users AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
