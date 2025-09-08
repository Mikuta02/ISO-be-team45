package iso.projekat.onlybunsbackend.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // === (postojeći deo) ADS preko fanout exchange-a ===
    public static final String AD_EXCHANGE = "adExchange";
    public static final String AD_QUEUE_1 = "adQueue1";
    public static final String AD_QUEUE_2 = "adQueue2";

    @Bean
    public FanoutExchange adExchange() { return new FanoutExchange(AD_EXCHANGE, true, false); }

    @Bean
    public Queue adQueue1() { return new Queue(AD_QUEUE_1, true); }

    @Bean
    public Queue adQueue2() { return new Queue(AD_QUEUE_2, true); }

    @Bean
    public Binding binding1(FanoutExchange adExchange, Queue adQueue1) {
        return BindingBuilder.bind(adQueue1).to(adExchange);
    }

    @Bean
    public Binding binding2(FanoutExchange adExchange, Queue adQueue2) {
        return BindingBuilder.bind(adQueue2).to(adExchange);
    }

    // === (NOVO) Rabbit care preko DIRECT exchange-a ===
    public static final String RABBIT_CARE_EXCHANGE = "rabbitCareExchange";
    public static final String RABBIT_CARE_QUEUE    = "rabbitCareQueue";
    public static final String RABBIT_CARE_ROUTING  = "rabbit.care.location";

    @Bean
    public DirectExchange rabbitCareExchange() {
        return new DirectExchange(RABBIT_CARE_EXCHANGE, true, false);
    }

    @Bean
    public Queue rabbitCareQueue() {
        return new Queue(RABBIT_CARE_QUEUE, true);
    }

    @Bean
    public Binding rabbitCareBinding(Queue rabbitCareQueue, DirectExchange rabbitCareExchange) {
        return BindingBuilder.bind(rabbitCareQueue)
                .to(rabbitCareExchange)
                .with(RABBIT_CARE_ROUTING);
    }

    // === zajednički converter i factory ===
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory cf,
                                                                            Jackson2JsonMessageConverter conv) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }
}
