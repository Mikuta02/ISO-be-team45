package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapDataDTO {
    private Double centerLatitude;
    private Double centerLongitude;
    private List<PostDTO> nearbyPosts;
    private List<LocationDTO> nearbyLocations;
}

