package org.lotus.carp.webssh.novnc.config;


import org.lotus.carp.webssh.novnc.controller.WebSshNoVncController;
import org.lotus.carp.webssh.novnc.websocket.NoVncWebSocketHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.lotus.carp.webssh.novnc.common.WebSshNoVncConst;
/**
 * @author : foy
 * @date : 2025/4/25:09:59
 **/
@Configuration
public class WebSshNoVncConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(WebSshNoVncConst.WEB_SSH_NO_VNC_PREFIX + "/**")
                //add vue2 dist folder to static resource
                .addResourceLocations(String.format("classpath:%s/", WebSshNoVncConst.WEB_SSH_NO_VNC_STATIC_FILE_ROOT));
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSshNoVncController webSshNoVncController(){
        return new WebSshNoVncController();
    }


    @Bean
    @ConditionalOnMissingBean
    public NoVncWebSocketHandler noVncWebSocketHandler(){
        return new NoVncWebSocketHandler();
    }
}
