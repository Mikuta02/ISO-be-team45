package iso.projekat.onlybunsbackend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS endpoint koji front koristi: http://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // front subscribes: /private-message/{me} i /topic/group/{id}
        config.enableSimpleBroker("/private-message", "/topic");
        // front šalje na /app/**
        config.setApplicationDestinationPrefixes("/app");
    }
}
