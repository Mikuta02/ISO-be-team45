package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDTO {
    private String description;
    private MultipartFile imagePath;
    private Double locationLatitude;
    private Double locationLongitude;
}
