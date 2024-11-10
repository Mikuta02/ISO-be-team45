package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final Logger logger = Logger.getLogger(PostService.class.getName());

    public List<PostDTO> getAllPosts() {
        logger.info("Fetching all posts");
        List<PostDTO> posts = postRepository.findAll().stream().map(PostDTO::new).collect(Collectors.toList());
        logger.info("Fetched all posts + " + posts.size());
        return posts;
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
