package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Follower;
import iso.projekat.onlybunsbackend.model.FollowerId;
import iso.projekat.onlybunsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follower, FollowerId> {
    Optional<Follower> findByUserAndFollower(User user, User followee);

    @Query("SELECT f.follower FROM Follower f WHERE f.user.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT f.user FROM Follower f WHERE f.follower.id = :followerId")
    List<User> findFollowingByFollowerId(@Param("followerId") Long followerId);


    @Query("SELECT COUNT(f) FROM Follower f WHERE f.user.id = :userId")
    int countFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.follower.id = :userId")
    int countFollowingByUserId(@Param("userId") Long userId);
}
