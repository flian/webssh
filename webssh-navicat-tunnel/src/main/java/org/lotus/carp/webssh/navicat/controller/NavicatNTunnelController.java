package org.lotus.carp.webssh.navicat.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.navicat.config.WebSshNavicatTunnelConst;
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

@RequestMapping(value = WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_MAPPING)
@Slf4j
public class NavicatNTunnelController extends BaseController {
    @Resource
    private WebSshLoginService webSshLoginService;


    @RequestMapping("/mysql")
    public String mysqlNTunnel(@RequestParam(value = "token",required = false) String token){
        ensureToken(webSshLoginService,token);
        return WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_MYSQL_FILE;
    }
    @RequestMapping("/pgsql")
    public String pgsqlNTunnel(@RequestParam(value = "token",required = false) String token){
        ensureToken(webSshLoginService,token);
        return WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_PGSQL_FILE;
    }
    @RequestMapping("/sqlite")
    public String sqliteNTunnel(@RequestParam(value = "token",required = false) String token){
        ensureToken(webSshLoginService,token);
        return WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_SQLITE_FILE;
    }
    @RequestMapping("/php_info")
    public String phpInfo(@RequestParam(value = "token",required = false) String token){
        ensureToken(webSshLoginService,token);
        return WebSshNavicatTunnelConst.WEB_SSH_PHP_INFO_FILE;
    }
}
