package org.lotus.carp.webssh.config.websocket.config;

import org.lotus.carp.webssh.config.controller.api.WebSshApi;
import org.lotus.carp.webssh.config.controller.api.WebSshFileApi;
import org.lotus.carp.webssh.config.controller.restful.DefaultWebSshController;
import org.lotus.carp.webssh.config.controller.restful.DefaultWebSshFileController;
import org.lotus.carp.webssh.config.service.WebSshFileService;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.config.service.WebSshTermService;
import org.lotus.carp.webssh.config.service.impl.DefaultJschWebSshFileServiceImpl;
import org.lotus.carp.webssh.config.service.impl.DefaultJschWebSshTermServiceImpl;
import org.lotus.carp.webssh.config.service.impl.DefaultWebSshLoginServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class DefaultBeanConfig {
    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshLoginService defaultLoginService() {
        return new DefaultWebSshLoginServiceImpl();
    }

    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshTermService defaultWebSshTermService(){
        return new DefaultJschWebSshTermServiceImpl();
    }

    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshFileService defaultWebSshFileService(){
        return new DefaultJschWebSshFileServiceImpl();
    }

    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshApi defaultApiController() {
        return new DefaultWebSshController();
    }

    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshFileApi defaultFileApiController() {
        return new DefaultWebSshFileController();
    }


}
