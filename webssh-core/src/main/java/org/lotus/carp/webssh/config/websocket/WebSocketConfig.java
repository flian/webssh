
package org.lotus.carp.webssh.config.websocket;

import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.config.websocket.websshenum.WebSshUrlCommandEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;


/**
 * <h3>WebSocketConfig</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 14:49
 **/
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSshConfig webSshConfig;

    @Resource
    private WebSshWebsocketHandler webSshHandler;
    @Resource
    private WebSshWebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSshHandler,
                        webSshConfig.getWebSshWebsocketPrefix() + WebSshUrlCommandEnum.TERM.getUrl(),
                        webSshConfig.getWebSshWebsocketPrefix() + WebSshUrlCommandEnum.FILE_UPLOAD_PROGRESS.getUrl())
                .setAllowedOrigins("*")
                .addInterceptors(webSocketHandshakeInterceptor);
    }
}