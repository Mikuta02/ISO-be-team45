package iso.projekat.onlybunsbackend.model;

import iso.projekat.onlybunsbackend.dto.CommentDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "comments", indexes = {
        @Index(name = "idx_comments_post_id", columnList = "post_id")
})
@NoArgsConstructor
public class Comment {
    @Id
    @ColumnDefault("nextval('comments_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author_id")
    private iso.projekat.onlybunsbackend.model.User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id")
    private iso.projekat.onlybunsbackend.model.Post post;

    public Comment(CommentDTO commentDTO) {
        this.content = commentDTO.getContent();
        this.post = new Post(); // Placeholder, proper post instance should be set in the service layer
        this.post.setId(commentDTO.getPostId());
        this.author = new User(); // Placeholder, proper author instance should be set in the service layer
        this.author.setId(commentDTO.getAuthorId());
    }
}