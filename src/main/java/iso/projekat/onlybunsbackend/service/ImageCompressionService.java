package iso.projekat.onlybunsbackend.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ImageCompressionService {

    public void compressImage(File originalImage, File compressedImage) throws IOException {
        Thumbnails.of(originalImage)
                .size(800, 600)
                .outputQuality(0.8)
                .toFile(compressedImage);
    }
}
