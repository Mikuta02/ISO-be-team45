package iso.projekat.onlybunsbackend.control;

import iso.projekat.onlybunsbackend.dto.LocationDTO;
import iso.projekat.onlybunsbackend.model.RabbitLocation;
import iso.projekat.onlybunsbackend.repository.RabbitLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rabbit-care")
@RequiredArgsConstructor
public class RabbitCareController {

    private final RabbitLocationRepository repo;

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> all() {
        List<LocationDTO> list = repo.findAll().stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<LocationDTO>> nearby(@RequestParam double lat,
                                                    @RequestParam double lng,
                                                    @RequestParam(defaultValue = "10") double radiusKm) {
        List<LocationDTO> list = repo.findAll().stream()
                .map(this::toDTO)
                .filter(d -> haversineKm(lat, lng, d.getLatitude(), d.getLongitude()) <= radiusKm)
                .toList();
        return ResponseEntity.ok(list);
    }

    private LocationDTO toDTO(RabbitLocation e) {
        return new LocationDTO(e.getName(), e.getLatitude(), e.getLongitude());
    }

    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0088;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
