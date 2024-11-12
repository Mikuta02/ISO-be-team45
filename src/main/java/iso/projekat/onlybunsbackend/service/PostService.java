package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.dto.UpdatePostDTO;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Logger logger = Logger.getLogger(PostService.class.getName());

    public List<PostDTO> getAllPosts() {
        logger.info("Fetching all posts");
        List<PostDTO> posts = postRepository.findAll().stream().map(PostDTO::new).collect(Collectors.toList());
        logger.info("Fetched all posts + " + posts.size());
        return posts;
    }

    public PostDTO getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(PostDTO::new).orElse(null);
    }

    public PostDTO createPost(PostDTO postDTO) {
        Post post = new Post(postDTO);
        postRepository.save(post);
        return new PostDTO(post);
    }

    public Post createPost(String description, String imagePath, Double latitude, Double longitude, String username) {
        Post post = new Post();
        post.setUser(userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found")));
        post.setDescription(description);
        post.setImagePath(imagePath);
        post.setLocationLatitude(latitude);
        post.setLocationLongitude(longitude);
        post.setCreatedAt(Instant.now());
        post.setLikesCount(0);

        return postRepository.save(post);
    }

    public PostDTO updatePost(Long id, UpdatePostDTO updatePostDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (updatePostDTO.getDescription() != null) {
            post.setDescription(updatePostDTO.getDescription());
        }
        if (updatePostDTO.getImagePath() != null) {
            post.setImagePath(updatePostDTO.getImagePath());
        }
        if (updatePostDTO.getLocationLatitude() != null) {
            post.setLocationLatitude(updatePostDTO.getLocationLatitude());
        }
        if (updatePostDTO.getLocationLongitude() != null) {
            post.setLocationLongitude(updatePostDTO.getLocationLongitude());
        }

        postRepository.save(post);
        return new PostDTO(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    public void likePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    public List<Post> getAllPostsSortedByDate() {
        return postRepository.findAll(PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
    }

    public List<PostDTO> getFollowingPosts(String username) {
        List<Post> postovi = postRepository.findByUser_Followers_Username(username);
        return postovi.stream().map(PostDTO::new).collect(Collectors.toList());
    }

    public List<PostDTO> getTrendingPosts() {
        return postRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "likesCount"))).stream().map(PostDTO::new).collect(Collectors.toList());
    }

    public List<Post> getNearbyPosts(double latitude, double longitude, double radiusKm) {
        return postRepository.findPostsByLocation(latitude, longitude, radiusKm);
    }
}
