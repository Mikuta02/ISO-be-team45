package iso.projekat.onlybunsbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rabbit_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitLocation {
    @Id
    private String id;
    private String name;
    private double latitude;
    private double longitude;
}
