/*
 * *
 *  * blog.coder4j.cn
 *  * Copyright (C) 2016-2019 All Rights Reserved.
 *
 */
package org.lotus.carp.webssh.config.websocket;

import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.config.websocket.websshenum.WebSshUrlCommandEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author buhao
 * @version WebSocketConfig.java, v 0.1 2019-10-17 15:43 buhao
 */
@Configuration
@EnableWebSocket
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