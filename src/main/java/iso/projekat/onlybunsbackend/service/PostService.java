package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(PostDTO::new).collect(Collectors.toList());
    }

    public PostDTO getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(PostDTO::new).orElse(null);
    }

    public PostDTO createPost(PostDTO postDTO) {
        Post post = new Post(postDTO);
        postRepository.save(post);
        return new PostDTO(post);
    }
}
