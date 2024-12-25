package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.CommentDTO;
import iso.projekat.onlybunsbackend.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public List<CommentDTO> getAllCommentsForPost(@PathVariable Long postId) {
        return commentService.getAllCommentsForPost(postId);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO, @RequestParam String username) {
        CommentDTO savedComment = commentService.createComment(commentDTO, username);
        return ResponseEntity.ok(savedComment);
    }
}