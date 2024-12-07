package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.enabled = false AND u.createdAt < CURRENT_DATE - 30")
    Optional<List<User>> findInactiveAccounts();
}
