package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<User, Long> {

    // Za rešavanje konkurentnosti (pessimistic lock)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(@Param("id") Long id);

    // Korisno ako do korisnika dolazimo preko username/email
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);
}
