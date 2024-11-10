package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDTO {
    private String description;
    private String imagePath;
    private Double locationLatitude;
    private Double locationLongitude;
}
