package iso.projekat.onlybunsbackend.service;


import iso.projekat.onlybunsbackend.dto.CommentDTO;
import iso.projekat.onlybunsbackend.model.Comment;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.model.User;
import iso.projekat.onlybunsbackend.repository.CommentRepository;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ConcurrentHashMap<String, List<Instant>> commentTimestamps = new ConcurrentHashMap<>();

    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!isAllowedToComment(username)) {
            throw new RuntimeException("You have exceeded the comment limit of 60 comments per hour.");
        }

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setCreatedAt(Instant.now());

        commentRepository.save(comment);
        registerCommentTimestamp(username);

        return new CommentDTO(comment);
    }

    private boolean isAllowedToComment(String username) {
        List<Instant> timestamps = commentTimestamps.getOrDefault(username, List.of());
        Instant oneHourAgo = Instant.now().minusSeconds(3600);
        long recentComments = timestamps.stream().filter(t -> t.isAfter(oneHourAgo)).count();
        return recentComments < 60;
    }

    private void registerCommentTimestamp(String username) {
        commentTimestamps.compute(username, (key, timestamps) -> {
            if (timestamps == null) {
                return List.of(Instant.now());
            } else {
                timestamps = timestamps.stream().filter(t -> t.isAfter(Instant.now().minusSeconds(3600))).collect(Collectors.toList());
                timestamps.add(Instant.now());
                return timestamps;
            }
        });
    }

    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }
}