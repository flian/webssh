/*
 * *
 *  * blog.coder4j.cn
 *  * Copyright (C) 2016-2019 All Rights Reserved.
 *
 */
package org.lotus.carp.webssh.config.websocket;

import org.lotus.carp.webssh.config.controller.api.Api;
import org.lotus.carp.webssh.config.controller.api.FileApi;
import org.lotus.carp.webssh.config.controller.restful.DefaultWebSshController;
import org.lotus.carp.webssh.config.controller.restful.DefaultWebSshFileController;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.config.service.impl.DefaultWebSshLoginServiceImpl;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
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
    private WebSshHandler webSshHandler;
    @Resource
    private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSshHandler, webSshConfig.getWebSshUri())
                .addInterceptors(webSocketHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}