package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.MapDataDTO;
import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.service.MonitoringService;
import iso.projekat.onlybunsbackend.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final MonitoringService monitoringService;

    // --- READ ---

    @GetMapping("/get")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/following")
    public ResponseEntity<List<PostDTO>> getFollowingPosts(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(postService.getFollowingPosts(authentication.getName()));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<PostDTO>> getTrendingPosts() {
        return ResponseEntity.ok(postService.getTrendingPosts());
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PostDTO>> getNearbyPosts(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusKm) {

        var posts = postService.getNearbyPosts(latitude, longitude, radiusKm);
        var postDTOs = posts.stream().map(PostDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/map")
    public ResponseEntity<MapDataDTO> getMapData(@RequestParam double latitude,
                                                 @RequestParam double longitude,
                                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body(null);
        }
        var mapData = postService.getMapDataForUser(authentication.getName(), latitude, longitude);
        return ResponseEntity.ok(mapData);
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> getMetrics() {
        return ResponseEntity.ok("Metrics available on /actuator/prometheus");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    // --- CREATE ---

    // JSON VARIJANTA — PREMEŠTENA na /json (da ne kolidira sa multipart)
    @PostMapping(
            path = "/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PostDTO> createPostJson(@RequestBody PostDTO postDTO, Authentication authentication) {
        monitoringService.updateActiveUsers(authentication.getName(), true);
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

    // MULTIPART VARIJANTA — OVO JE JEDINI POST na /api/posts
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PostDTO createPostMultipart(
            @RequestPart("image") MultipartFile image,
            @RequestParam("description") String description,
            @RequestParam("locationLatitude") Double locationLatitude,
            @RequestParam("locationLongitude") Double locationLongitude,
            Principal principal
    ) {
        return postService.createPost(principal, image, description, locationLatitude, locationLongitude);
    }

    // (Opcionalno) binarni fallback — ostaje na odvojenom path-u
    @PostMapping(
            path = "/binary",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO createPostBinary(HttpServletRequest request,
                                    @RequestParam("description") String description,
                                    @RequestParam("locationLatitude") Double locationLatitude,
                                    @RequestParam("locationLongitude") Double locationLongitude,
                                    Principal principal) throws IOException {
        byte[] data = request.getInputStream().readAllBytes();
        return postService.createPostFromBytes(principal, data, "image/jpeg", description, locationLatitude, locationLongitude);
    }

    // --- UPDATE / DELETE / LIKE ---

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long id,
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body(null);
        }

        var updatedPost = postService.updatePost(id, description, latitude, longitude, image, authentication.getName());
        return ResponseEntity.ok(new PostDTO(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body("You must be logged in to like posts");
        }
        postService.likePost(id, authentication.getName());
        return ResponseEntity.ok("Post liked successfully");
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body("You must be logged in to like posts");
        }
        postService.unlikePost(postId, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
