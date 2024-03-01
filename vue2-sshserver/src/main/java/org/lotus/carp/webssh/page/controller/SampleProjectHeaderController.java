package org.lotus.carp.webssh.page.controller;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.utils.RandomUtils;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderParamVo;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderRequestVo;
import org.lotus.carp.webssh.page.restful.DefaultWebSshProjectTokensController;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <h3>webssh</h3>
 * <p>sample for project header.</p>
 *
 * @author : foy
 * @date : 2024-03-01 10:37
 **/
@Controller
public class SampleProjectHeaderController extends DefaultWebSshProjectTokensController {
    private String projectExchangeTokenResult = "EXCHANGE_TOKEN_RESULT";

    public SampleProjectHeaderController() {
        if (super.defaultProjectHeaders.isEmpty()) {
            //sample add 2 test header token and value
            //project include me can take as a sample
            defaultProjectHeaders.add(new ProjectHeaderParamVo("AUTH_COOKIE_TEST", RandomUtils.generatePassword(8)));
            defaultProjectHeaders.add(new ProjectHeaderParamVo("JUST_DO_TEST", "Bearer " + RandomUtils.generatePassword()));
        }
    }

    private boolean haseProjectExchangeTokenResult() {
        return defaultProjectHeaders.stream().filter(d -> d.getName().equals(projectExchangeTokenResult)).findFirst().isPresent();
    }

    /**
     * just a sample do project token exchange
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @Override
    public WebSshResponse<List<ProjectHeaderParamVo>> composeProjectHeaderTokens(HttpServletRequest request, HttpServletResponse response, ProjectHeaderRequestVo requestVo) {
        //return headers.
        if (!ObjectUtils.isEmpty(requestVo.getProjectExchangeToken())) {
            if (!haseProjectExchangeTokenResult()) {
                defaultProjectHeaders.add(new ProjectHeaderParamVo(projectExchangeTokenResult, "PROJECT_TOKEN_EXCHANGE_RESULT:12345"));
            }
        }
        return super.composeProjectHeaderTokens(request, response, requestVo);
    }
}
