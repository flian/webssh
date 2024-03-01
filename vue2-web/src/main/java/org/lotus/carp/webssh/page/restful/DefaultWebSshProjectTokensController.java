package org.lotus.carp.webssh.page.restful;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.controller.vo.WebSshRequestBase;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.page.api.WebSshProjectTokensApi;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderParamVo;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderRequestVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public WebSshResponse<List<ProjectHeaderParamVo>> composeProjectHeaderTokens(HttpServletRequest request, HttpServletResponse response, ProjectHeaderRequestVo requestVo) {
        WebSshResponse<List<ProjectHeaderParamVo>> result = checkTokenValid(requestVo);
        if (null != result) {
            return result;
        }
        return WebSshResponse.ok(defaultProjectHeaders);
    }

    protected WebSshResponse<List<ProjectHeaderParamVo>> checkTokenValid(ProjectHeaderRequestVo requestVo) {
        if (!webSshLoginService.isTokenValid(requestVo.getToken())) {
            return WebSshResponse.fail("token is invalid.");
        }
        return null;
    }
}
