package org.lotus.carp.webssh.proxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.restful.BaseController;

import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.proxy.config.WebSshHttpProxyServerComponent;
import org.lotus.carp.webssh.proxy.config.WebSshSocketProxyServerComponent;
import org.lotus.carp.webssh.navicat.config.WebSshNavicatTunnelConst;
import org.lotus.carp.webssh.proxy.controller.vo.ProxyOpRequestVo;
import org.lotus.carp.webssh.proxy.controller.vo.TunnelAndProxyInfoResultVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : foy
 * @date : 2025/4/16:15:35
 **/
@RestController
@RequestMapping(value = "/webssh/tunnelAndProxy")
@Slf4j
public class TunnelAndProxyInfoController extends BaseController {
    @Resource
    private WebSshLoginService webSshLoginService;
    @Resource
    private WebSshHttpProxyServerComponent webSshHttpProxyServerConfig;

    @Resource
    private WebSshSocketProxyServerComponent webSshSocketProxyServerConfig;
    void validateToken(String token) {
        if (!webSshLoginService.isTokenValid(token)) {
            log.error("token is not valid..");
            throw new WebSshBusinessException("invalid access. reason: invalid token.");
        }
    }

    @PostMapping("/updateProxy")
    public WebSshResponse<Boolean> updateProxyInfo(@RequestParam(value = "token", required = false) String token,
                                                   @RequestBody ProxyOpRequestVo requestVo){
        validateToken(token);
        webSshHttpProxyServerConfig.updateProxy(requestVo);
        webSshSocketProxyServerConfig.updateProxy(requestVo);
        return WebSshResponse.ok(Boolean.TRUE);
    }

    @RequestMapping("/info")
    public WebSshResponse<List<TunnelAndProxyInfoResultVo>> tunnelAndProxyInfo(
            HttpServletRequest request,
            @RequestParam(value = "token", required = false) String token){
        validateToken(token);
        //http or https
        String schema = request.getScheme();
        //host info here
        String host = request.getServerName();
        //server port info
        int port = request.getServerPort();
        log.info("schema:{},host:{},port:{}",schema,host,port);
        List<TunnelAndProxyInfoResultVo> result = new ArrayList<>();
        String urlPrefix = String.format("%s://%s:%s",schema,host,port);
        result.add(createDbTunnel(urlPrefix,"mysql",token));
        result.add(createDbTunnel(urlPrefix,"pgsql",token));
        result.add(createDbTunnel(urlPrefix,"sqlite",token));
        TunnelAndProxyInfoResultVo phpInfo = createDbTunnel(urlPrefix,"php_info",token);
        phpInfo.setType(TunnelAndProxyInfoResultVo.LINK_TYPE);
        result.add(phpInfo);

        //http proxy info
        result.add(httpProxy(schema,host));

        //socket proxy info
        result.add(socketProxy(host));

        return WebSshResponse.ok(result);
    }

    private TunnelAndProxyInfoResultVo socketProxy(String host){
        TunnelAndProxyInfoResultVo result = new TunnelAndProxyInfoResultVo();
        result.setMeme("socketProxy");
        result.setType(TunnelAndProxyInfoResultVo.DEFAULT_TYPE);
        result.setHost(host);
        result.setPort(webSshSocketProxyServerConfig.getPort());
        result.setTunnel(false);
        result.setRunning(webSshSocketProxyServerConfig.isServerStarted());
        return result;
    }
    private TunnelAndProxyInfoResultVo httpProxy(String protocol,String host){
        TunnelAndProxyInfoResultVo result = new TunnelAndProxyInfoResultVo();
        result.setMeme("httpProxy");
        result.setType(TunnelAndProxyInfoResultVo.DEFAULT_TYPE);
        result.setUrl(String.format("%s://%s:%s",protocol, host,webSshHttpProxyServerConfig.proxyPort()));
        result.setTunnel(false);
        result.setRunning(webSshHttpProxyServerConfig.isServerStarted());
        return result;
    }

    private TunnelAndProxyInfoResultVo createDbTunnel(String urlPrefix,String meme,String token){
        TunnelAndProxyInfoResultVo result = new TunnelAndProxyInfoResultVo();
        result.setMeme(meme);
        result.setType(TunnelAndProxyInfoResultVo.DEFAULT_TYPE);
        result.setUrl(String.format("%s%s/%s?token=%s",urlPrefix, WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_MAPPING,meme,token));
        result.setTunnel(true);
        result.setRunning(Boolean.TRUE);
        return result;
    }
}
