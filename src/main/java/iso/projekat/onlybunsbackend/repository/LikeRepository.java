package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Like;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
}
