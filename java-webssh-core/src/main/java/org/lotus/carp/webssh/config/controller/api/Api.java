package org.lotus.carp.webssh.config.controller.api;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.CheckRequestParamsVo;
import org.lotus.carp.webssh.config.controller.vo.CheckResponseVo;
import org.lotus.carp.webssh.config.controller.vo.LoginVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginResultVo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 17:27
 **/
@RequestMapping("/webssh")
public interface Api {

    /**
     * check sshinfo and token etc。
     * @param checkRequestParamsVo
     * @return
     */
    @GetMapping("/check")
    @ResponseBody
    WebSshResponse<CheckResponseVo> check(CheckRequestParamsVo checkRequestParamsVo);

    /**
     * should skip login
     * @return
     */
    @GetMapping("/shouldVerifyToken")
    @ResponseBody
    WebSshResponse<Boolean> shouldVerifyToken();

    /**
     * handle user login
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    WebSshResponse<WebSshLoginResultVo> handleLogin(@RequestBody LoginVo loginVo, HttpServletRequest request);
}
