package org.lotus.carp.webssh.config.websocket.config;

import org.lotus.carp.webssh.config.controller.api.WebSshApi;
import org.lotus.carp.webssh.config.controller.api.WebSshFileApi;
import org.lotus.carp.webssh.config.controller.restful.DefaultWebSshController;
import org.lotus.carp.webssh.config.controller.restful.DefaultWebSshFileController;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.lotus.carp.webssh.config.service.WebSshJavaRdpService;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.config.service.WebSshTermService;
import org.lotus.carp.webssh.config.service.impl.DefaultJschWebSshFileServiceImpl;
import org.lotus.carp.webssh.config.service.impl.DefaultJschWebSshTermServiceImpl;
import org.lotus.carp.webssh.config.service.impl.DefaultWebSshLoginServiceImpl;
import org.lotus.carp.webssh.config.websocket.WebSocketConfig;
import org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor;
import org.lotus.carp.webssh.config.websocket.WebSshWebsocketHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 15:42
 **/
@Configuration
@EnableConfigurationProperties(WebSshConfig.class)
public class DefaultBeanConfig {

    @ConditionalOnMissingBean
    @Bean
    public WebSshJavaRdpService defaultWebSshJavaRdpService() {
        return new WebSshJavaRdpService(){};
    }


    @ConditionalOnMissingBean
    @Bean
    public WebSshLoginService defaultLoginService() {
        return new DefaultWebSshLoginServiceImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public WebSshTermService defaultWebSshTermService(){
        return new DefaultJschWebSshTermServiceImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public WebSshFileService defaultWebSshFileService(){
        return new DefaultJschWebSshFileServiceImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public WebSshApi defaultApiController() {
        return new DefaultWebSshController();
    }

    @ConditionalOnMissingBean
    @Bean
    public WebSshFileApi defaultFileApiController() {
        return new DefaultWebSshFileController();
    }


    @ConditionalOnMissingBean
    @Bean
    public WebSocketConfig defaultWebSocketConfig(){
        return new WebSocketConfig();
    }

    @ConditionalOnMissingBean
    @Bean
    public WebSshWebsocketHandler defaultWebSshWebsocketHandler(){
        return new WebSshWebsocketHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public WebSshWebSocketHandshakeInterceptor defaultWebSshWebSocketHandshakeInterceptor(){
        return new WebSshWebSocketHandshakeInterceptor();
    }
}
