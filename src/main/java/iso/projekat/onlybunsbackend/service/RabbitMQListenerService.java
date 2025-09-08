package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.RabbitLocationDTO;
import iso.projekat.onlybunsbackend.model.RabbitLocation;
import iso.projekat.onlybunsbackend.repository.RabbitLocationRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQListenerService {

    private final RabbitLocationRepository locationRepository;

    @RabbitListener(queues = "rabbitCareQueue")
    public void receiveLocationMessage(RabbitLocationDTO dto) {
        String id = (dto.getId() == null || dto.getId().isBlank())
                ? RandomString.make(16)
                : dto.getId();

        RabbitLocation location = new RabbitLocation(
                id,
                dto.getName(),
                dto.getLatitude(),
                dto.getLongitude()
        );
        locationRepository.save(location);
    }
}
