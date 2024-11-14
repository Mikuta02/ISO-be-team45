package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.dto.UpdatePostDTO;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final Logger logger = Logger.getLogger(PostController.class.getName());

    private final PostService postService;

    @GetMapping("/get")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long id,
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) {

        // Proveravamo da li je korisnik autentifikovan
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body(null);
        }

        // Kreiranje UpdatePostDTO i pozivanje PostService za ažuriranje posta
        Post updatedPost = postService.updatePost(id, description, latitude, longitude, image, authentication.getName());
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

        List<Post> posts = postService.getNearbyPosts(latitude, longitude, radiusKm);
        List<PostDTO> postDTOs = posts.stream().map(PostDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {
        logger.info("Creating post with description: " + description);
        logger.info("Latitude: " + latitude + ", Longitude: " + longitude);
        logger.info("Image: " + image.getOriginalFilename());
        // Save the image to the server
        String imagePath = saveImage(image);

        // Create post with all the provided details
        Post createdPost = postService.createPost(description, imagePath, latitude, longitude, authentication.getName());

        return ResponseEntity.ok(new PostDTO(createdPost));
    }


    private String saveImage(MultipartFile image) {
        try {
            // Koristimo apsolutnu putanju da bismo izbegli greške sa relativnim putanjama
            String uploadDir = System.getProperty("user.dir") + "/uploaded_images/";
            File uploadFolder = new File(uploadDir);

            // Kreiramo direktorijum ako ne postoji
            if (!uploadFolder.exists()) {
                boolean mkdirs = uploadFolder.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("Failed to create directory for storing images");
                }
            }

            // Generišemo jedinstveno ime fajla i kreiramo fajl objekat
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File file = new File(uploadDir + fileName);

            // Log info o putanji gde ćemo sačuvati sliku
            logger.info("Saving image to: " + file.getAbsolutePath());

            // Sačuvamo fajl sa transferTo metodom MultipartFile objekta
            image.transferTo(file);

            // Vraćamo putanju slike koju kasnije možemo koristiti za prikaz
            return "uploaded_images/" + fileName;

        } catch (IOException e) {
            // Ukoliko dođe do greške, logujemo i bacamo RuntimeException
            logger.log(Level.ALL, "Failed to store image", e);
            throw new RuntimeException("Failed to store image", e);
        }
    }

}