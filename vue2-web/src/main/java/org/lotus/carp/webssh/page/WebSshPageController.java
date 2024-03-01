package org.lotus.carp.webssh.page;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.page.common.WebSshVue2PageConst;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h3>webssh</h3>
 * <p>web ssh page controller</p>
 *
 * @author : foy
 * @date : 2024-02-27 10:25
 **/


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

    private String prefixParameterName = "prefix";

    /**
     * index with prefix parameter
     */
    private static String INDEX_STR = "index.html";

    protected Map<String, String> defaultIndexPageRedirectParams = new HashMap<>();

    @GetMapping(WebSshVue2PageConst.WEB_SSH_VUE2_INDEX)
    public String index(HttpServletRequest request) {

        HashMap<String, String> indexNeedParams = new HashMap<>();
        indexNeedParams.putAll(defaultIndexPageRedirectParams);

        String projectExchangeToken = null;
        if (!urlContainsProjectExchangeToken(request)) {
            projectExchangeToken = generateProjectExchangeToken(request);
            if (!ObjectUtils.isEmpty(projectExchangeToken)) {
                //need project exchange token.
                indexNeedParams.put(WebSshVue2PageConst.WEB_SSH_VUE2_PROJECT_EXCHANGE_TOKEN, projectExchangeToken);
            }
        }
        processOtherParamsNeed(request, indexNeedParams);

        boolean shouldRender = shouldRenderOrRedirect(request, indexNeedParams);

        if (shouldRender) {
            //render page
            return String.format("%s/%s", WebSshVue2PageConst.WEB_SSH_PREFIX, INDEX_STR);
        } else {
            //or redirect request with prefix parameter.
            return String.format("redirect:%s?%s", WebSshVue2PageConst.WEB_SSH_VUE2_INDEX, composeGetQueryStr(request, indexNeedParams));
        }
    }

    /**
     * @param request
     * @param indexNeedParams
     * @return true: render index page;
     * false: send redirect with indexNeedParams parameters
     */
    public boolean shouldRenderOrRedirect(HttpServletRequest request, Map<String, String> indexNeedParams) {
        boolean shouldRender = true;
        for (String k : indexNeedParams.keySet()) {
            if (!isUrlContainKey(request, k)) {
                shouldRender = false;
            }
        }
        return shouldRender;
    }

    public void processOtherParamsNeed(HttpServletRequest request, Map<String, String> indexNeedParams) {
        //sub class can add more params need pass to page..
    }

    public String composeGetQueryStr(HttpServletRequest request, Map<String, String> requestParams) {
        List<String> paramsStrList = new ArrayList<>();
        for (String k : requestParams.keySet()) {
            String val = requestParams.get(k);
            if (!ObjectUtils.isEmpty(val)) {
                paramsStrList.add(String.format("%s=%s", k, val));
            }
        }
        return paramsStrList.stream().collect(Collectors.joining("&"));
    }

    public boolean isUrlContainKey(HttpServletRequest request, String key) {
        String queryStr = request.getQueryString();
        if (null == queryStr || queryStr.isEmpty()) {
            return false;
        }
        return queryStr.contains(key);
    }

    public boolean urlContainsProjectExchangeToken(HttpServletRequest request) {
        return isUrlContainKey(request, WebSshVue2PageConst.WEB_SSH_VUE2_PROJECT_EXCHANGE_TOKEN);
    }


    public String generateProjectExchangeToken(HttpServletRequest request) {
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //if context path is set, always need prefix.
        if (!isRootCxtEmpty(contextPath)) {
            defaultIndexPageRedirectParams.put(prefixParameterName, contextPath);
        }
        if (!isRootCxtEmpty(webSshConfigContextPrefix)) {
            defaultIndexPageRedirectParams.put(prefixParameterName, webSshConfigContextPrefix);
        }
    }

    private boolean isRootCxtEmpty(String ctxPath) {
        return (null == ctxPath || ctxPath.isEmpty() || "/".equals(ctxPath));
    }

}
