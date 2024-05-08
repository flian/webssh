package org.lotus.carp.webssh.navicat;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.navicat.config.WebSshNavicatTunnelConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-08 10:26
 **/
@Controller
@RequestMapping(value = WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_MAPPING)
@Slf4j
public class NavicatNTunnelController {
    @Resource
    private WebSshLoginService webSshLoginService;

    void validateToken(String token){
        if(!webSshLoginService.isTokenValid(token)){
            log.error("token is not valid..");
            throw  new WebSshBusinessException("invalid access. reason: invalid token.");
        }
    }
    @GetMapping("/mysql")
    public String mysqlNTunnel(@RequestParam(value = "token",required = false) String token){
        validateToken(token);
        return WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_MYSQL_FILE;
    }
    @GetMapping("/pgsql")
    public String pgsqlNTunnel(@RequestParam(value = "token",required = false) String token){
        validateToken(token);
        return WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_PGSQL_FILE;
    }
    @GetMapping("/sqlite")
    public String sqliteNTunnel(@RequestParam(value = "token",required = false) String token){
        validateToken(token);
        return WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_SQLITE_FILE;
    }
}
