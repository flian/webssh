package org.lotus.carp.webssh.page.restful;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.controller.vo.WebSshRequestBase;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.page.api.WebSshProjectTokensApi;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderParamVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>webssh</h3>
 * <p>default project tokens impl</p>
 *
 * @author : foy
 * @date : 2024-03-01 10:30
 **/
public class DefaultWebSshProjectTokensController extends BaseController implements WebSshProjectTokensApi {

    protected List<ProjectHeaderParamVo> defaultProjectHeaders = new ArrayList<>();

    @Resource
    private WebSshLoginService webSshLoginService;

    @Override
    public WebSshResponse<List<ProjectHeaderParamVo>> composeProjectHeaderTokens(WebSshRequestBase requestVo) {
        WebSshResponse<List<ProjectHeaderParamVo>> result = checkTokenValid(requestVo);
        if (null != result) {
            return result;
        }
        return WebSshResponse.ok(defaultProjectHeaders);
    }

    protected WebSshResponse<List<ProjectHeaderParamVo>> checkTokenValid(WebSshRequestBase requestVo) {
        if (!webSshLoginService.isTokenValid(requestVo.getToken())) {
            return WebSshResponse.fail("token is invalid.");
        }
        return null;
    }
}
