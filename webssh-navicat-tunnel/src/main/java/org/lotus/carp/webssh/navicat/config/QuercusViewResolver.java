package org.lotus.carp.webssh.navicat.config;

import com.mongodb.lang.NonNullApi;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-08 11:30
 **/
public class QuercusViewResolver extends UrlBasedViewResolver {
    public QuercusViewResolver() {
        setViewClass(requiredViewClass());
    }


    @Override
    protected Class<?>  requiredViewClass() {
        return QuercusView.class;
    }
}
