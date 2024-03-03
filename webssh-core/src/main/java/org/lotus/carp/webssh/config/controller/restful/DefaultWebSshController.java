package org.lotus.carp.webssh.config.controller.restful;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.api.WebSshApi;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.CheckRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.CheckResponseVo;
import org.lotus.carp.webssh.config.controller.vo.LoginVo;
import org.lotus.carp.webssh.config.controller.vo.LogoutVo;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginResultVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLogoutResultVo;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.lotus.carp.webssh.config.controller.common.WebSshAppConst.DEFAULT_WEB_SSH_SUCCESS_MSG;

/**
 * <h3>javaWebSSH</h3>
 * <p>webssh controller</p>
 *
 * @author : foy
 * @date : 2024-01-31 17:13
 **/

@Slf4j
public class DefaultWebSshController extends BaseController implements WebSshApi {

    @Resource
    private WebSshConfig webSshConfig;

    @Resource
    private WebSshLoginService loginService;

    @Override
    public WebSshResponse<CheckResponseVo> check(CheckRequestParamsVo checkRequestParamsVo) {
        CheckResponseVo checkResponseVo = new CheckResponseVo();
        checkResponseVo.setShouldVerifyToken(webSshConfig.isShouldVerifyToken());
        checkResponseVo.setSavePass(webSshConfig.getSavePass());
        return WebSshResponse.ok(checkResponseVo,DEFAULT_WEB_SSH_SUCCESS_MSG);
    }

    @Override
    public WebSshResponse<Boolean> shouldVerifyToken() {
        return WebSshResponse.ok(webSshConfig.isShouldVerifyToken());
    }

    @Override
    public WebSshResponse<WebSshLoginResultVo> handleLogin(@Valid @RequestBody LoginVo loginVo, HttpServletRequest request) {
        WebSshLoginVo webSshLoginVo = new WebSshLoginVo();
        webSshLoginVo.setUsername(loginVo.getUsername());
        webSshLoginVo.setPassword(loginVo.getPassword());
        webSshLoginVo.setRequestIp(request.getRemoteHost());
        WebSshLoginResultVo result = loginService.doWebSshLogin(webSshLoginVo);
        if (null == result) {
            result = WebSshLoginResultVo.emptyOne();
        }
        return WebSshResponse.ok(result);
    }

    @Override
    public WebSshResponse<WebSshLogoutResultVo> handleLogout(LogoutVo logoutVo, HttpServletRequest request) {
        logoutVo.setRequestIp(request.getRemoteHost());
        return WebSshResponse.ok(loginService.doWebSshLogout(logoutVo));
    }

}
