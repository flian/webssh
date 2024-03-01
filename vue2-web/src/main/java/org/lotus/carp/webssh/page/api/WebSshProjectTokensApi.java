package org.lotus.carp.webssh.page.api;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.WebSshRequestBase;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderParamVo;
import org.lotus.carp.webssh.page.api.vo.ProjectHeaderRequestVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <h3>webssh</h3>
 * <p>support web ssh project token for front page</p>
 *
 * @author : foy
 * @date : 2024-03-01 10:12
 **/
@RequestMapping("/webssh/projectHeader")
public interface WebSshProjectTokensApi {
    /**
     * @param request
     * @param response
     * @param requestVo
     * @return
     */
    @GetMapping("/params")
    @ResponseBody
    default WebSshResponse<List<ProjectHeaderParamVo>> composeProjectHeaderTokens(HttpServletRequest request, HttpServletResponse response, ProjectHeaderRequestVo requestVo) {
        throw new WebSshBusinessException("Please implements me!");
    }
}
