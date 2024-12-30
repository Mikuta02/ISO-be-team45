package iso.projekat.onlybunsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AdMessage {
    private String description;
    private Instant createdAt;
    private Long userId;
}
