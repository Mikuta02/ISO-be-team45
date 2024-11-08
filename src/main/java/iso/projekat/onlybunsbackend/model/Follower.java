package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "followers", indexes = {
        @Index(name = "idx_followers_user_id", columnList = "user_id")
})
public class Follower {
    @EmbeddedId
    private FollowerId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private iso.projekat.onlybunsbackend.model.User user;

    @MapsId("followerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "follower_id", nullable = false)
    private iso.projekat.onlybunsbackend.model.User follower;

}