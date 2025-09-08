package iso.projekat.onlybunsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class UploadStorageService {

    @Value("${app.upload.dir:uploaded_images}")
    private String uploadDir;

    @Value("${app.upload.public-base-url:http://localhost:8080/uploaded_images/}")
    private String publicBaseUrl;

    public String saveAndGetPublicUrl(String filename, byte[] data, String contentType) {
        try {
            Path dir = Path.of(uploadDir);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            Files.write(target, data);
            return publicBaseUrl + filename;
        } catch (IOException e) {
            throw new RuntimeException("Unable to persist uploaded file", e);
        }
    }

    public String saveAndGetLocalUrl(String filename, byte[] data, String contentType) {
        try {
            Path dir = Path.of(uploadDir);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            Files.write(target, data);
            return uploadDir + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Unable to persist uploaded file", e);
        }
    }
}
