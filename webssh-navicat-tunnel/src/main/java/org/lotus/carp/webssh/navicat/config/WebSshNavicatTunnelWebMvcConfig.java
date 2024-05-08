package org.lotus.carp.webssh.navicat.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
@Order(Integer.MIN_VALUE)
public class WebSshNavicatTunnelWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_MAPPING + "/**")
                //add vue2 dist folder to static resource
                .addResourceLocations(String.format("classpath:%s/", WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_PHP_FOLDER));
    }
}
