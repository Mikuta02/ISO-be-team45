package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.RabbitLocationDTO;
import iso.projekat.onlybunsbackend.model.RabbitLocation;
import iso.projekat.onlybunsbackend.repository.RabbitLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitMQListenerService {

    private RabbitLocationRepository locationRepository;

    @RabbitListener(queues = "rabbitCareQueue")
    public void receiveLocationMessage(RabbitLocationDTO locationDTO) {
        RabbitLocation location = new RabbitLocation(
                locationDTO.getId(),
                locationDTO.getName(),
                locationDTO.getLatitude(),
                locationDTO.getLongitude()
        );
        locationRepository.save(location);
        System.out.println("Saved location: " + location.getName());
    }
}
