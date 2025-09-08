package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.NetworkTrends;
import iso.projekat.onlybunsbackend.dto.UserLikesDTO;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.repository.LikeRepository;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;          // <-- ispravan import
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendsService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    private List<Post> cacheTopWeeklyPosts;
    private List<Post> cacheTopAllTimePosts;
    private Instant cacheWeeklyTs;
    private Instant cacheAllTimeTs;

    public NetworkTrends getTrends() {
        Instant now = Instant.now();
        Instant weekAgo = now.minusSeconds(7L * 24 * 3600);
        Instant monthAgo = now.minusSeconds(30L * 24 * 3600);

        long totalPosts = postRepository.count();
        long postsLastMonth = postRepository.countByCreatedAtGreaterThanEqual(monthAgo);

        List<Post> topPostsLastWeek = getWeeklyTopCached(weekAgo);
        List<Post> topPostsAllTime = getAllTimeTopCached();

        var views = likeRepository.findTopLikersSince(weekAgo);
        var topUsersLastWeek = views.stream()
                .map(v -> new UserLikesDTO(v.getUserId(), v.getUsername(), v.getLikesGiven()))
                .toList();

        return new NetworkTrends(totalPosts, postsLastMonth, topPostsLastWeek, topPostsAllTime, topUsersLastWeek);
    }

    private List<Post> getWeeklyTopCached(Instant since) {
        var now = Instant.now();
        if (cacheTopWeeklyPosts == null || cacheWeeklyTs == null || cacheWeeklyTs.plusSeconds(2 * 60).isBefore(now)) {
            cacheTopWeeklyPosts = postRepository
                    .findByCreatedAtAfterOrderByLikesCountDesc(since, PageRequest.of(0, 5)); // <-- bez kastovanja
            cacheWeeklyTs = now;
        }
        return cacheTopWeeklyPosts;
    }

    private List<Post> getAllTimeTopCached() {
        var now = Instant.now();
        if (cacheTopAllTimePosts == null || cacheAllTimeTs == null || cacheAllTimeTs.plusSeconds(5 * 60).isBefore(now)) {
            cacheTopAllTimePosts = postRepository.findTop10ByOrderByLikesCountDesc();
            cacheAllTimeTs = now;
        }
        return cacheTopAllTimePosts;
    }
}
