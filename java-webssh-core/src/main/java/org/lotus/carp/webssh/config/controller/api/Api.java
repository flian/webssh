package org.lotus.carp.webssh.config.controller.api;

import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.vo.LoginVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginResultVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * handle user login
     * @param loginVo
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    WebSshResponse<WebSshLoginResultVo> handleLogin(@RequestBody LoginVo loginVo, HttpServletRequest request);
}
