package iso.projekat.onlybunsbackend.dto;

import java.util.List;
import iso.projekat.onlybunsbackend.model.Post;
import lombok.Getter;


public class NetworkTrends {
    // --- getters & setters ---
    private long totalPosts;
    private long postsLastMonth;
    private List<PostDTO> topPostsLastWeek;
    private List<PostDTO> topPostsAllTime;
    private List<UserLikesDTO> topUsersLastWeek;

    public NetworkTrends(long totalPosts,
                         long postsLastMonth,
                         List<Post> topPostsLastWeek,
                         List<Post> topPostsAllTime,
                         List<UserLikesDTO> topUsersLastWeek) {
        this.totalPosts = totalPosts;
        this.postsLastMonth = postsLastMonth;
        this.topPostsLastWeek = topPostsLastWeek.stream().map(PostDTO::new).toList();
        this.topPostsAllTime = topPostsAllTime.stream().map(PostDTO::new).toList();
        this.topUsersLastWeek = topUsersLastWeek;
    }

    public void setTotalPosts(long totalPosts) { this.totalPosts = totalPosts; }

    public void setPostsLastMonth(long postsLastMonth) { this.postsLastMonth = postsLastMonth; }

    public void setTopPostsLastWeek(List<PostDTO> topPostsLastWeek) { this.topPostsLastWeek = topPostsLastWeek; }

    public void setTopPostsAllTime(List<PostDTO> topPostsAllTime) { this.topPostsAllTime = topPostsAllTime; }

    public void setTopUsersLastWeek(List<UserLikesDTO> topUsersLastWeek) { this.topUsersLastWeek = topUsersLastWeek; }

    public long getTotalPosts() {
        return totalPosts;
    }

    public long getPostsLastMonth() {
        return postsLastMonth;
    }

    public List<PostDTO> getTopPostsLastWeek() {
        return topPostsLastWeek;
    }

    public List<PostDTO> getTopPostsAllTime() {
        return topPostsAllTime;
    }

    public List<UserLikesDTO> getTopUsersLastWeek() {
        return topUsersLastWeek;
    }
}
