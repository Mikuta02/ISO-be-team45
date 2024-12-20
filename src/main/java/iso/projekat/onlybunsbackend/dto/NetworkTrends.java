package iso.projekat.onlybunsbackend.dto;

import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.model.User;
import lombok.Data;

import java.util.List;

@Data
public class NetworkTrends {
    private long totalPosts;
    private long postsLastMonth;
    private List<PostDTO> topPostsLastWeek;
    private List<PostDTO> topPostsAllTime;
    private List<UserDTO> topUsersLastWeek;

    public NetworkTrends(long totalPosts, long postsLastMonth, List<Post> topPostsLastWeek, List<Post> topPostsAllTime, List<User> topUsersLastWeek) {
        this.totalPosts = totalPosts;
        this.postsLastMonth = postsLastMonth;
        this.topPostsLastWeek = topPostsLastWeek.stream().map(PostDTO::new).toList();
        this.topPostsAllTime = topPostsAllTime.stream().map(PostDTO::new).toList();
        this.topUsersLastWeek = topUsersLastWeek.stream().map(UserDTO::new).toList();
    }
}
