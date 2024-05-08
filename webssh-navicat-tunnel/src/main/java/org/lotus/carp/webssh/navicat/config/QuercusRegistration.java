package org.lotus.carp.webssh.navicat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.UrlBasedViewResolverRegistration;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-08 11:27
 **/
@Component
public class QuercusRegistration extends UrlBasedViewResolverRegistration {
    public QuercusRegistration() {
        super(new QuercusViewResolver());
        getViewResolver().setSuffix(".php");
    }
}
