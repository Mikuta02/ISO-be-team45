package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.model.Follower;
import iso.projekat.onlybunsbackend.repository.FollowRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FollowService {
    private FollowRepository followRepository;

    private UserRepository userRepository;

    public String followUser(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            return "Cannot follow yourself!";
        }
        Follower follow = new Follower();
        follow.setFollower(userRepository.findById(followerId).get());
        follow.setUser(userRepository.findById(followeeId).get());
        followRepository.save(follow);
        return "Followed successfully!";
    }

    public String unfollowUser(Long followerId, Long followeeId) {
        Optional<Follower> follow = followRepository.findByUserAndFollower(
                userRepository.findById(followerId).get(),
                userRepository.findById(followeeId).get()
        );
        if (follow.isPresent()) {
            followRepository.delete(follow.get());
            return "Unfollowed successfully!";
        }
        return "Follow relationship not found!";
    }
}
