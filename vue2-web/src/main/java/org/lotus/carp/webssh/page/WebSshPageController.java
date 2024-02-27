package org.lotus.carp.webssh.page;

import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <h3>webssh</h3>
 * <p>web ssh page controller</p>
 *
 * @author : foy
 * @date : 2024-02-27 10:25
 **/

@Controller
@ConditionalOnProperty(value = "webssh.vue2.pageController.enable", matchIfMissing = true)
public class WebSshPageController {
    private String staticFileRoot = "webssh-dist";

    @GetMapping(WebSshVue2PageConst.WEB_SSH_VUE2_INDEX)
    public String index() {
        return String.format("%s/%s", staticFileRoot, "index.html");
    }

    @GetMapping("/webssh/static/{staticType}/{staticFileFull}")
    public String staticJsCssEtc(@PathVariable String staticType, @PathVariable String staticFileFull) {
        return String.format("%s/%s/%s", staticFileRoot, staticType, staticFileFull);
    }
}
