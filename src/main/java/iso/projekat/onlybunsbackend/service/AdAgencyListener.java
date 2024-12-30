package iso.projekat.onlybunsbackend.service;

import iso.projekat.onlybunsbackend.dto.AdMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AdAgencyListener {

    @RabbitListener(queues = "adQueue1")
    public void handleAdFromQueue1(AdMessage adMessage) {
        System.out.println("Agency 1 received ad: " + adMessage);
    }

    @RabbitListener(queues = "adQueue2")
    public void handleAdFromQueue2(AdMessage adMessage) {
        System.out.println("Agency 2 received ad: " + adMessage);
    }
}
