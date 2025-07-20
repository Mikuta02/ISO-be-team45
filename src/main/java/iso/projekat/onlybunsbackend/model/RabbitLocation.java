package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "rabbit_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RabbitLocation {
    @Id
    private String id;
    private String name;
    private double latitude;
    private double longitude;

    public RabbitLocation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
