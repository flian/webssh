package org.lotus.carp.webssh.navicat.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.navicat.config.WebSshNavicatTunnelConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-13 09:50
 **/
@Controller
@Slf4j
public class MongoAdminController extends BaseController {
    @Resource
    private WebSshLoginService webSshLoginService;

    void validateToken(String token) {
        if (!webSshLoginService.isTokenValid(token)) {
            log.error("token is not valid..");
            throw new WebSshBusinessException("invalid access. reason: invalid token.");
        }
    }

    @RequestMapping(WebSshNavicatTunnelConst.WEB_SSH_MONGO_ADMIN_URL)
    public String mongoAdmin(@RequestParam(value = "token", required = false) String token) {
        validateToken(token);
        return WebSshNavicatTunnelConst.WEB_SSH_MONGO_ADMIN_PAGE;
    }

}
