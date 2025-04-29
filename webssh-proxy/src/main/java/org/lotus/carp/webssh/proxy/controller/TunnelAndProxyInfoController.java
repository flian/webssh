package org.lotus.carp.webssh.proxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.controller.common.WebSshResponse;
import org.lotus.carp.webssh.config.controller.restful.BaseController;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.proxy.config.WebSshHttpProxyServerComponent;
import org.lotus.carp.webssh.proxy.config.WebSshProxyConstants;
import org.lotus.carp.webssh.proxy.config.WebSshSocketProxyServerComponent;
import org.lotus.carp.webssh.proxy.controller.vo.ProxyOpRequestVo;
import org.lotus.carp.webssh.proxy.controller.vo.TunnelAndProxyInfoItemResultVo;
import org.lotus.carp.webssh.proxy.controller.vo.TunnelAndProxyItemResultVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : foy
 * @date : 2025/4/16:15:35
 **/
@ResponseBody
@RequestMapping(value = "/webssh/tunnelAndProxy")
@Slf4j
public class TunnelAndProxyInfoController extends BaseController {
    @Resource
    private WebSshLoginService webSshLoginService;
    @Resource
    private WebSshHttpProxyServerComponent webSshHttpProxyServerConfig;

    @Resource
    private WebSshSocketProxyServerComponent webSshSocketProxyServerConfig;


    @PostMapping("/updateProxy")
    public WebSshResponse<Boolean> updateProxyInfo(@RequestParam(value = "token", required = false) String token,
                                                   @RequestBody ProxyOpRequestVo requestVo){
        ensureToken(webSshLoginService,token);
        webSshHttpProxyServerConfig.updateProxy(requestVo);
        webSshSocketProxyServerConfig.updateProxy(requestVo);
        return WebSshResponse.ok(Boolean.TRUE);
    }

    @RequestMapping("/info")
    public WebSshResponse<TunnelAndProxyItemResultVo> tunnelAndProxyInfo(
            HttpServletRequest request,
            @RequestParam(value = "token", required = false) String token){
        ensureToken(webSshLoginService,token);
        //http or https
        String schema = request.getScheme();
        //host info here
        String host = request.getServerName();
        //server port info
        int port = request.getServerPort();
        log.info("schema:{},host:{},port:{}",schema,host,port);
        TunnelAndProxyItemResultVo result = new TunnelAndProxyItemResultVo();
        result.setSchema(schema);
        result.setHost(host);
        result.setPort(port);
        List<TunnelAndProxyInfoItemResultVo> items = new ArrayList<>();
        result.setItems(items);
        //http proxy info
        items.add(httpProxy(schema,host));
        //socket proxy info
        items.add(socketProxy(host));

        return WebSshResponse.ok(result);
    }

    private TunnelAndProxyInfoItemResultVo socketProxy(String host){
        TunnelAndProxyInfoItemResultVo result = new TunnelAndProxyInfoItemResultVo();
        result.setMeme("socketProxy");
        result.setProxyType(WebSshProxyConstants.SOCKET_PROXY_TYPE);
        result.setHost(webSshSocketProxyServerConfig.getProxyBindIp());
        result.setPort(webSshSocketProxyServerConfig.getPort());
        result.setUsername(webSshSocketProxyServerConfig.getProxyUserName());
        result.setPassword(webSshSocketProxyServerConfig.getProxyPassword());
        result.setRunning(webSshSocketProxyServerConfig.isServerStarted());
        return result;
    }
    private TunnelAndProxyInfoItemResultVo httpProxy(String protocol,String host){
        TunnelAndProxyInfoItemResultVo result = new TunnelAndProxyInfoItemResultVo();
        result.setMeme("httpProxy");
        result.setProxyType(WebSshProxyConstants.HTTP_PROXY_TYPE);
        result.setHttpProxyUrl(String.format("%s://%s:%s",protocol, host,webSshHttpProxyServerConfig.proxyPort()));
        result.setHost(webSshHttpProxyServerConfig.getProxyBindIp());
        result.setPort(webSshHttpProxyServerConfig.proxyPort());
        result.setUsername(webSshHttpProxyServerConfig.getProxyUserName());
        result.setPassword(webSshHttpProxyServerConfig.getProxyPassword());
        result.setRunning(webSshHttpProxyServerConfig.isServerStarted());
        return result;
    }
}
