package org.lotus.carp.webssh.page;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * get servlet context root path
     */
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * config context path in case under is not tomcat?
     */
    @Value("${webssh.api.url.prefix:}")
    private String webSshConfigContextPrefix;

    private boolean shouldForwardWithPrefixParameters = false;

    private String prefixParameterName = "prefix";

    private static final String INDEX_STR_WITH_PARAMETER = "%s?%s=%s";
    /**
     * index with prefix parameter
     */
    private static String INDEX_STR = "index.html";

    private String forwardIndexUri = "%s"+WebSshVue2PageConst.WEB_SSH_VUE2_INDEX + "?%s=%s";


    @GetMapping(WebSshVue2PageConst.WEB_SSH_VUE2_INDEX)
    public String index(HttpServletRequest request) {
        if (shouldRenderPage(request)) {
            //render page
            return String.format("/%s/%s", WebSshVue2PageConst.WEB_SSH_PREFIX, INDEX_STR);
        } else {
            //or forward request with prefix parameter.
            return String.format("forward:%s", forwardIndexUri);
        }
    }

    private boolean shouldRenderPage(HttpServletRequest request){
        if(!shouldForwardWithPrefixParameters){
            return true;
        }

        String queryStr = request.getQueryString();
        if(null == queryStr || queryStr.isEmpty()){
            return false;
        }

        if(queryStr.contains(prefixParameterName)){
            return true;
        }

        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isRootCxtEmpty(contextPath) && isRootCxtEmpty(webSshConfigContextPrefix)) {
            log.info("not set context path,ignore prefix parameter to front.");
            return;
        } else {
            shouldForwardWithPrefixParameters = true;
        }
        if (!isRootCxtEmpty(contextPath)) {
            INDEX_STR = String.format(INDEX_STR_WITH_PARAMETER, INDEX_STR, prefixParameterName, contextPath);
            forwardIndexUri = String.format(forwardIndexUri, contextPath,prefixParameterName, contextPath);
            return;
        } else if (!isRootCxtEmpty(webSshConfigContextPrefix)) {
            INDEX_STR = String.format(INDEX_STR_WITH_PARAMETER, INDEX_STR, prefixParameterName, webSshConfigContextPrefix);
            forwardIndexUri = String.format(forwardIndexUri, webSshConfigContextPrefix,prefixParameterName, webSshConfigContextPrefix);
        }
    }

    private boolean isRootCxtEmpty(String ctxPath) {
        return (null == contextPath || contextPath.isEmpty() || "/".equals(contextPath));
    }

    /*@GetMapping("/webssh/static/{staticType}/{staticFileFull}")
    public String staticJsCssEtc(@PathVariable String staticType, @PathVariable String staticFileFull) {
        return String.format("/%s/%s/%s", WebSshVue2PageConst.WEB_SSH_PREFIX, staticType, staticFileFull);
    }*/
}
