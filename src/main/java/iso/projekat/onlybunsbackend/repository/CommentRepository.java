package iso.projekat.onlybunsbackend.repository;

import iso.projekat.onlybunsbackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
