package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class FollowerId implements java.io.Serializable {
    private static final long serialVersionUID = -441638483847793968L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FollowerId entity = (FollowerId) o;
        return Objects.equals(this.followerId, entity.followerId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, userId);
    }

}