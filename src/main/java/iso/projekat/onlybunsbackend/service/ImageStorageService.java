package iso.projekat.onlybunsbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    private static final String UPLOAD_DIR = "uploaded_images";

    public String storeImage(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());

        return "/uploaded_images/" + fileName; // URL koji čuvaš u bazi
    }

    public String storeImage(byte[] bytes, String extension) throws IOException {
        String fileName = System.currentTimeMillis() + "_upload." + extension;
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, bytes);

        return "/uploaded_images/" + fileName;
    }
}