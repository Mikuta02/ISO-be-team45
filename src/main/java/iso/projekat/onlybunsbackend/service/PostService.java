package iso.projekat.onlybunsbackend.service;

import io.micrometer.core.annotation.Timed;
import iso.projekat.onlybunsbackend.dto.LocationDTO;
import iso.projekat.onlybunsbackend.dto.MapDataDTO;
import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.model.Like;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.model.RabbitLocation;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.LikeRepository;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.RabbitLocationRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private MonitoringService monitoringService;
    private RabbitLocationRepository locationRepository;
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

    @Transactional
    @Timed(value = "http.requests.create_post")
    public PostDTO createPost(PostDTO postDTO) {
        long start = System.currentTimeMillis();
        try {
            Post post = new Post(postDTO);
            postRepository.save(post);
            return new PostDTO(post);
        } finally {
            long duration = System.currentTimeMillis() - start;
            monitoringService.recordPostCreationTime(duration);
        }
    }

    @Transactional
    @Timed(value = "http.requests.create_post")
    public Post createPost(String description, String imagePath, Double latitude, Double longitude, String username) {
        long start = System.currentTimeMillis();
        Post post = new Post();
        post.setUser(userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found")));
        post.setDescription(description);
        post.setImagePath(imagePath);
        post.setLocationLatitude(latitude);
        post.setLocationLongitude(longitude);
        post.setCreatedAt(Instant.now());
        post.setLikesCount(0);

        try {
            post = postRepository.save(post);
            return post;
        } finally {
            long duration = System.currentTimeMillis() - start;
            monitoringService.recordPostCreationTime(duration);
        }
    }

    public Post updatePost(Long postId, String description, Double latitude, Double longitude,
                           MultipartFile image, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not have permission to edit this post");
        }

        // Ažuriranje detalja posta
        post.setDescription(description);
        post.setLocationLatitude(latitude);
        post.setLocationLongitude(longitude);

        // Ako je nova slika izabrana, sačuvamo je
        if (image != null && !image.isEmpty()) {
            try {
                String newImagePath = saveImage(image);
                post.setImagePath(newImagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }

        return postRepository.save(post);
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploaded_images/";
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists()) {
            boolean mkdirs = uploadFolder.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("Failed to create directory for storing images");
            }
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File file = new File(uploadDir + fileName);
        image.transferTo(file);

        return "uploaded_images/" + fileName;
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    @Transactional
    public void likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            throw new RuntimeException("You have already liked this post");
        }

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        // Increment the like count
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like existingLike = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("You haven't liked this post yet"));

        likeRepository.delete(existingLike);

        post.setLikesCount(post.getLikesCount() - 1);
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

    public MapDataDTO getMapDataForUser(String username, double latitude, double longitude) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> nearbyPosts = postRepository.findPostsByLocation(latitude, longitude, 10.0);
        List<PostDTO> postDTOs = nearbyPosts.stream().map(PostDTO::new).collect(Collectors.toList());

        List<RabbitLocation> locations = locationRepository.findAll();
        List<LocationDTO> locationDTOs = locations.stream().map(loc -> new LocationDTO(loc.getName(), loc.getLatitude(), loc.getLongitude()))
                .collect(Collectors.toList());

        return new MapDataDTO(latitude, longitude, postDTOs, locationDTOs);
    }

    public List<PostDTO> getPostsByUser(Long userId) {
        return postRepository.findPostsByUser(userId)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }


}
