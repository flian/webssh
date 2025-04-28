package org.lotus.carp.webssh.novnc.config;

import org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.novnc.websocket.NoVncWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author : foy
 * @date : 2025/4/25:14:30
 **/
@Configuration
@EnableWebSocket
public class WebSshNoVncWebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSshConfig webSshConfig;
    @Resource
    private WebSshWebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Resource
    private NoVncWebSocketHandler noVncWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(noVncWebSocketHandler, "/websockify",webSshConfig.getWebSshNoVncWebsocketPrefix())
                .setAllowedOrigins("*")
                .addInterceptors(webSocketHandshakeInterceptor);
    }

}