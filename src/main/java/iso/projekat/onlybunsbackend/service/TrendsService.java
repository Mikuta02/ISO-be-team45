package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.NetworkTrends;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendsService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private List<Post> cachedPopularPosts;
    private Instant lastCacheUpdate;

    public NetworkTrends getTrends() {
        long totalPosts = postRepository.count();
        long postsLastMonth = postRepository.countPostsInLastDays(Instant.now().minusSeconds(30L * 24 * 3600));
        List<Post> topPostsLastWeek = getCachedPopularPosts();
        List<Post> topPostsAllTime = postRepository.findTop10ByOrderByLikesCountDesc();
        List<User> topUsersLastWeek = userRepository.findTop10UsersByLikesLastWeek(Instant.now().minusSeconds(7L * 24 * 3600));

        return new NetworkTrends(totalPosts, postsLastMonth, topPostsLastWeek, topPostsAllTime, topUsersLastWeek);
    }

    private List<Post> getCachedPopularPosts() {
        if (cachedPopularPosts == null || lastCacheUpdate == null || lastCacheUpdate.plusSeconds(3600).isBefore(Instant.now())) {
            cachedPopularPosts = postRepository.findTop5ByLikesInLastDays(Instant.now().minusSeconds(7L * 24 * 3600));
            lastCacheUpdate = Instant.now();
        }
        return cachedPopularPosts;
    }

}
