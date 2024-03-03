package org.lotus.carp.webssh.page.controller;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.utils.RandomUtils;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderRequestVo;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderParamVo;
import org.lotus.carp.webssh.page.restful.DefaultWebSshProjectTokensController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>webssh</h3>
 * <p>sample for project header.</p>
 *
 * @author : foy
 * @date : 2024-03-01 10:37
 **/
@Controller
public class SampleProjectHeaderController extends DefaultWebSshProjectTokensController implements InitializingBean {

    @Resource
    protected WebSshConfig webSshConfig;

    private String projectExchangeTokenResult = "EXCHANGE_TOKEN_RESULT";

    @Value("${webssh.enableTestProjectHeader:false}")
    private boolean enableTestProjectHeader;

    public SampleProjectHeaderController() {

    }

    private boolean haseProjectExchangeTokenResult(List<ProjectHeaderParamVo> list) {
        return list.stream().filter(d -> d.getName().equals(projectExchangeTokenResult)).findFirst().isPresent();
    }

    /**
     * just a sample to show case for project exchange token.
     *
     * @param requestVo
     * @return
     */
    @Override
    protected WebSshResponse<List<ProjectHeaderParamVo>> checkTokenValidAndComposeResult(HttpServletRequest request, HttpServletResponse response, ProjectHeaderRequestVo requestVo) {
        WebSshResponse<List<ProjectHeaderParamVo>> superResult = super.checkTokenValidAndComposeResult(request, response, requestVo);
        if (!superResult.isOk()) {
            //valid token is not ok,just return.
            return superResult;
        }
        List<ProjectHeaderParamVo> resultList = new ArrayList<>();
        resultList.addAll(superResult.getData());
        //return headers.
        if (webSshConfig.isEnableProjectExchangeToken() && !ObjectUtils.isEmpty(requestVo.getProjectExchangeToken())) {
            if (!haseProjectExchangeTokenResult(resultList)) {
                //if request project token exchange,add a result.just for show case.
                resultList.add(new ProjectHeaderParamVo(projectExchangeTokenResult, "PROJECT_TOKEN_EXCHANGE_RESULT:12345"));
            }
        }
        return WebSshResponse.ok(resultList);
    }

    @Override
    public void afterPropertiesSet() {
        if (enableTestProjectHeader && super.defaultProjectHeaders.isEmpty()) {
            //sample add 2 test header token and value
            //project include me can take as a sample
            defaultProjectHeaders.add(new ProjectHeaderParamVo("AUTH_COOKIE_TEST", RandomUtils.generatePassword(8)));
            defaultProjectHeaders.add(new ProjectHeaderParamVo("JUST_DO_TEST", "Bearer " + RandomUtils.generatePassword()));
        }
    }
}
