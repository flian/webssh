package org.lotus.carp.webssh.novnc.config;

import org.lotus.carp.webssh.novnc.websocket.NoVncWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author : foy
 * @date : 2025/4/25:14:30
 **/
@Configuration
@EnableWebSocket
public class WebSshNoVncWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(noVncWebSocketHandler(), "/novnc/websockify")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler noVncWebSocketHandler() {
        return new NoVncWebSocketHandler();
    }
}