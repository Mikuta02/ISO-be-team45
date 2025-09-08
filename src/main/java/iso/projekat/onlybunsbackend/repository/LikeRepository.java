package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Like;
import iso.projekat.onlybunsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, iso.projekat.onlybunsbackend.model.Post post);
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    interface UserLikesView {
        Long getUserId();
        Long getLikesGiven();
        String getUsername();
    }

    @Query(value = """
            SELECT l.user_id    AS userId,
                   COUNT(*)     AS likesGiven,
                   u.username   AS username
            FROM likes l
            JOIN users u ON u.id = l.user_id
            WHERE l.created_at >= :since
            GROUP BY l.user_id, u.username
            ORDER BY likesGiven DESC
            LIMIT 10
        """, nativeQuery = true)
    List<UserLikesView> findTopLikersSince(@Param("since") Instant since);
}
