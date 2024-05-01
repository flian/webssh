package org.lotus.carp.webssh.config.controller.api;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.*;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginResultVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLogoutResultVo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 17:27
 **/
@RequestMapping("/webssh")
public interface WebSshApi {

    /**
     * check sshinfo and token etc。
     * should use
     * @param checkRequestParamsVo
     *
     * @return defaultConfig instead.
     */
    @GetMapping("/check")
    @ResponseBody
    @Deprecated
    default WebSshResponse<CheckResponseVo> check(CheckRequestParamsVo checkRequestParamsVo) {
        throw new WebSshBusinessException("Please implements me!");
    }

    /**
     * default config for system
     * @param defaultConfigRequestParamsVo
     * @return
     */
    @GetMapping("/systemDefaultConfig")
    @ResponseBody
    default WebSshResponse<DefaultConfigVo> defaultConfig(DefaultConfigRequestParamsVo defaultConfigRequestParamsVo) {
        throw new WebSshBusinessException("Please implements me!");
    }

    /**
     * should skip login
     *
     * @return
     */
    @GetMapping("/shouldVerifyToken")
    @ResponseBody
    default WebSshResponse<Boolean> shouldVerifyToken() {
        throw new WebSshBusinessException("Please implements me!");
    }

    /**
     * handle user login
     *
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    default WebSshResponse<WebSshLoginResultVo> handleLogin(@Valid @RequestBody LoginVo loginVo, HttpServletRequest request) {
        throw new WebSshBusinessException("Please implements me!");
    }

    @GetMapping("/logout")
    @ResponseBody
    default WebSshResponse<WebSshLogoutResultVo> handleLogout(LogoutVo logoutVo, HttpServletRequest request) {
        throw new WebSshBusinessException("Please implements me!");
    }

}
