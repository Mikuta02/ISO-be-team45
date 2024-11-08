package iso.projekat.onlybunsbackend.model;

import iso.projekat.onlybunsbackend.dto.PostDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_user_id", columnList = "user_id")
})
@NoArgsConstructor
public class Post {
    @Id
    @ColumnDefault("nextval('posts_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @NotNull
    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "location_latitude")
    private Double locationLatitude;

    @Column(name = "location_longitude")
    private Double locationLongitude;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("0")
    @Column(name = "likes_count")
    private Integer likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private iso.projekat.onlybunsbackend.model.User user;

    public Post(PostDTO postDTO) {
        this.description = postDTO.getDescription();
        this.imagePath = postDTO.getImagePath();
        this.locationLatitude = postDTO.getLocationLatitude();
        this.locationLongitude = postDTO.getLocationLongitude();
        this.likesCount = postDTO.getLikesCount();
        this.user = new User(); // Placeholder, proper user instance should be set in the service layer
        this.user.setId(postDTO.getUserId());
    }

}