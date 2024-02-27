package org.lotus.carp.webssh.page;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <h3>webssh</h3>
 * <p>web ssh page controller</p>
 *
 * @author : foy
 * @date : 2024-02-27 10:25
 **/

@Controller
@ConditionalOnProperty(value = "webssh.vue2.pageController.enable", matchIfMissing = true)
@Slf4j
public class WebSshPageController implements InitializingBean {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * index with prefix parameter
     */
    private static String INDEX_STR = "index.html";


    @GetMapping(WebSshVue2PageConst.WEB_SSH_VUE2_INDEX)
    public String index() {
        return String.format("/%s/%s", WebSshVue2PageConst.WEB_SSH_PREFIX, INDEX_STR);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null == contextPath || contextPath.isEmpty() || "/".equals(contextPath)) {
            log.info("not set context path,ignore prefix parameter to front.");
            return;
        }
        INDEX_STR = INDEX_STR + "?prefix=" + contextPath;
    }

    /*@GetMapping("/webssh/static/{staticType}/{staticFileFull}")
    public String staticJsCssEtc(@PathVariable String staticType, @PathVariable String staticFileFull) {
        return String.format("/%s/%s/%s", WebSshVue2PageConst.WEB_SSH_PREFIX, staticType, staticFileFull);
    }*/
}
