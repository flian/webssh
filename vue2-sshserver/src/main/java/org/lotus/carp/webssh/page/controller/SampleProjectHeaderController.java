package org.lotus.carp.webssh.page.controller;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.WebSshRequestBase;
import org.lotus.carp.webssh.config.utils.RandomUtils;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderParamVo;
import org.lotus.carp.webssh.page.restful.DefaultWebSshProjectTokensController;
import org.springframework.stereotype.Controller;

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
    public SampleProjectHeaderController() {
        if (super.defaultProjectHeaders.isEmpty()) {
            //sample add 2 test header token and value
            //project include me can take as a sample
            defaultProjectHeaders.add(new ProjectHeaderParamVo("AUTH_COOKIE_TEST", RandomUtils.generatePassword(8)));
            defaultProjectHeaders.add(new ProjectHeaderParamVo("JUST_DO_TEST", "Bearer " + RandomUtils.generatePassword()));
        }
    }

    @Override
    public WebSshResponse<List<ProjectHeaderParamVo>> composeProjectHeaderTokens(WebSshRequestBase requestVo) {
        //return headers.
        return super.composeProjectHeaderTokens(requestVo);
    }
}
