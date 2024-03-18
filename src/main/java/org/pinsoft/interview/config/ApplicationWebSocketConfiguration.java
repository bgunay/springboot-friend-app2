package org.pinsoft.interview.config;

import lombok.RequiredArgsConstructor;
import org.pinsoft.interview.service.UserService;
import org.pinsoft.interview.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class ApplicationWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    /**
     * Register Stomp endpoints: the url to open the WebSocket connection.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                .setUserDestinationPrefix("/user")
                .enableSimpleBroker("/chat", "/topic", "/queue");
    }


}
