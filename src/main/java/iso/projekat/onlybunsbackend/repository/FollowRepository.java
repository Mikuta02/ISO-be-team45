package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Follower;
import iso.projekat.onlybunsbackend.model.FollowerId;
import iso.projekat.onlybunsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follower, FollowerId> {
    Optional<Follower> findByUserAndFollower(User user, User followee);

}
