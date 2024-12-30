package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.AdMessage;
import iso.projekat.onlybunsbackend.dto.PostDTO;
import iso.projekat.onlybunsbackend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/ads")
@AllArgsConstructor
public class AdController {

    private final RabbitTemplate rabbitTemplate;
    private final PostService postService;

    @PostMapping("/{postId}/promote")
    public ResponseEntity<?> promotePost(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPostById(postId);

        if (postDTO == null) {
            return ResponseEntity.badRequest().body("Post not found");
        }

        AdMessage adMessage = new AdMessage(
                postDTO.getDescription(),
                new Date().toInstant(),
                postDTO.getUserId()
        );

        rabbitTemplate.convertAndSend("adExchange", "", adMessage);
        return ResponseEntity.ok("Post promoted successfully");
    }
}
