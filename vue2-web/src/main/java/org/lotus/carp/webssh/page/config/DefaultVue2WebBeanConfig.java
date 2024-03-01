package org.lotus.carp.webssh.page.config;

/**
 * <h3>webssh</h3>
 * <p>default vue2 page beans</p>
 *
 * @author : foy
 * @date : 2024-03-01 10:35
 **/

import org.lotus.carp.webssh.page.WebSshPageController;
import org.lotus.carp.webssh.page.api.WebSshProjectTokensApi;
import org.lotus.carp.webssh.page.restful.DefaultWebSshProjectTokensController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class DefaultVue2WebBeanConfig {
    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshProjectTokensApi defaultWebSshProjectTokens() {
        return new DefaultWebSshProjectTokensController();
    }

    @ConditionalOnProperty(value = "webssh.vue2.pageController.enable", matchIfMissing = true)
    @ConditionalOnMissingBean
    @Resource
    @Bean
    public WebSshPageController defaultWebSshPageController() {
        return new WebSshPageController();
    }
}
