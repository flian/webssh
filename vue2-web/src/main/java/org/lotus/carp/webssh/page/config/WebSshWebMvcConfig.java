package org.lotus.carp.webssh.page.config;

import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h3>webssh</h3>
 * <p>web ssh web mvc config</p>
 *
 * @author : foy
 * @date : 2024-02-27 11:33
 **/
@Configuration
@ConditionalOnProperty(value = "webssh.vue2.pageController.enable", matchIfMissing = true)
public class WebSshWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(WebSshVue2PageConst.WEB_SSH_PREFIX + "/**")
                //add vue2 dist folder to static resource
                .addResourceLocations(String.format("classpath:%s/", WebSshVue2PageConst.WEB_SSH_VUE2_STATIC_FILE_ROOT));
    }
}
