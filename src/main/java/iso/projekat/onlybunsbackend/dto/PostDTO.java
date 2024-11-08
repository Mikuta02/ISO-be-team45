package iso.projekat.onlybunsbackend.dto;

import iso.projekat.onlybunsbackend.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String description;
    private String imagePath;
    private Double locationLatitude;
    private Double locationLongitude;
    private Long userId;
    private Integer likesCount;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.imagePath = post.getImagePath();
        this.locationLatitude = post.getLocationLatitude();
        this.locationLongitude = post.getLocationLongitude();
        this.userId = post.getUser().getId();
        this.likesCount = post.getLikesCount();
    }
}
