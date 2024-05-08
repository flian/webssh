package org.lotus.carp.webssh.navicat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.ViewResolver;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-08 14:55
 **/
@Configuration
@Order(Integer.MIN_VALUE)
public class QuercusConfig {
    @Bean
    public ViewResolver resolver(){
        QuercusViewResolver quercusViewResolver = new QuercusViewResolver();
        quercusViewResolver.setPrefix("php");
        quercusViewResolver.setSuffix(".php");
        quercusViewResolver.setOrder(0);
        return quercusViewResolver;
    }
}
