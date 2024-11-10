package iso.projekat.onlybunsbackend.scheduled;

import iso.projekat.onlybunsbackend.model.Post;
import iso.projekat.onlybunsbackend.repository.PostRepository;
import iso.projekat.onlybunsbackend.service.ImageCompressionService;
import iso.projekat.onlybunsbackend.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduledImageCompressionService {
    private final ImageCompressionService imageCompressionService;
    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Svaki dan u ponoć
    public void compressOldImages() {
        List<Post> posts = postRepository.findAll(); // Ovdje dodati filter za slike starije od mjesec dana
        for (Post post : posts) {
            try {
                File originalImage = new File(post.getImagePath());
                File compressedImage = new File(post.getCompressedImagePath());
                if (!compressedImage.exists()) {
                    imageCompressionService.compressImage(originalImage, compressedImage);
                    System.out.println("Slika " + originalImage.getName() + " kompresovana.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
