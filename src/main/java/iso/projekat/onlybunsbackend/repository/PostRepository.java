package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.user.id IN (SELECT f.id.followerId FROM Follower f WHERE f.user.username = :username)")
    List<Post> findByUser_Followers_Username(String username);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.location_latitude)) * " +
            "cos(radians(p.location_longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(p.location_latitude)))) < :radius ", nativeQuery = true)
    List<Post> findPostsByLocation(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );

    List<Post> findAllByUser(User user);

    // All-time top 10
    List<Post> findTop10ByOrderByLikesCountDesc();

    // Broj postova u poslednjih 30 dana (nema @Query, derived metoda)
    long countByCreatedAtGreaterThanEqual(Instant since);

    // Top za poslednjih 7 dana (uz Pageable)
    List<Post> findByCreatedAtAfterOrderByLikesCountDesc(Instant since, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.createdAt > :since")
    long countPostsByUserSince(@Param("userId") Long userId, @Param("since") Instant since);

    @Query("SELECT SUM(p.likesCount) FROM Post p WHERE p.user.id = :userId AND p.createdAt > :since")
    long countLikesOnUserPosts(@Param("userId") Long userId, @Param("since") Instant since);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findPostsByUser(@Param("userId") Long userId);

    List<Post> findByUser_IdIn(List<Long> userIds, Sort sort);

}

