package iso.projekat.onlybunsbackend.dto;

import iso.projekat.onlybunsbackend.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String description;
    private String image; // Slika koja se šalje sa fronta kao MultipartFile
    private Double locationLatitude;
    private Double locationLongitude;
    private Long userId;
    private Integer likesCount;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.locationLatitude = post.getLocationLatitude();
        this.locationLongitude = post.getLocationLongitude();
        this.userId = post.getUser().getId();
        this.likesCount = post.getLikesCount();
        this.image = "http://localhost:8080/" + post.getImagePath();
    }
}
