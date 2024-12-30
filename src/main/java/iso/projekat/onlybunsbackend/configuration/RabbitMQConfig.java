package iso.projekat.onlybunsbackend.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue rabbitCareQueue() {
        return new Queue("rabbitCareQueue", true);
    }
}
