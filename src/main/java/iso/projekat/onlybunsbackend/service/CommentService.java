package iso.projekat.onlybunsbackend.service;


import iso.projekat.onlybunsbackend.dto.CommentDTO;
import iso.projekat.onlybunsbackend.model.Comment;
import iso.projekat.onlybunsbackend.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        return commentRepository.findAll().stream()
                .filter(comment -> comment.getPost().getId().equals(postId))
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment(commentDTO);
        comment.setId(0L);
        commentRepository.save(comment);
        return new CommentDTO(comment);
    }
}