package org.lotus.carp.webssh.proxy.config;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.proxy.controller.TunnelAndProxyInfoController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author : foy
 * @date : 2025/4/28:15:07
 **/
@Configuration
@Slf4j
public class WebSshProxyConfig {

    @Bean
    @ConditionalOnMissingBean
    public WebSshHttpProxyServerComponent httpProxyServerComponent(){
        return new WebSshHttpProxyServerComponent();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSshSocketProxyServerComponent socketProxyServerComponent(){
        return new WebSshSocketProxyServerComponent();
    }

    @Bean
    @ConditionalOnMissingBean
    public TunnelAndProxyInfoController tunnelAndProxyInfoController(){
        return new TunnelAndProxyInfoController();
    }
}
