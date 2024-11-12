package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Post;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreatedAtBefore(LocalDate date);

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
}

