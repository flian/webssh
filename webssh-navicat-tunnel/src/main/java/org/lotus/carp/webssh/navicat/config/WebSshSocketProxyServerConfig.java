package org.lotus.carp.webssh.navicat.config;

import lombok.extern.slf4j.Slf4j;
import org.bbottema.javasocksproxyserver.SocksServer;
import org.bbottema.javasocksproxyserver.auth.UsernamePasswordAuthenticator;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author : foy
 * @date : 2025/4/15:16:30
 **/

@Component
@Slf4j
public class WebSshSocketProxyServerConfig implements InitializingBean {
    protected SocksServer socksProxyServer;
    @Resource
    protected WebSshConfig webSshConfig;

    public String getPort() {
        return "" + webSshConfig.getSocketProxyPort();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!webSshConfig.isEnableSocketProxy()) {
            log.info("enableSocketProxy is false. server will not start socket proxy.");
            return;
        }
        log.info(String.format("starting java socket5 proxy server on port:%s..., enabled NO_AUTH:%s", webSshConfig.getSocketProxyPort(),webSshConfig.isSocketNoAuthProxy()));
        if (webSshConfig.isDebugHttpProxy()) {
            log.info(String.format("socket5 proxy username:%s,password:%s"
                    , webSshConfig.getSocketProxyUserName(), webSshConfig.getSocketProxyPassword()));
        }

        socksProxyServer = new SocksServer(webSshConfig.getSocketProxyPort()).setAuthenticator(new UsernamePasswordAuthenticator(webSshConfig.isSocketNoAuthProxy()) {
            @Override
            public boolean validate(String username, String password) {
                return webSshConfig.getSocketProxyUserName().equals(username)
                        && password.equals(webSshConfig.getSocketProxyPassword());
            }
        });


        socksProxyServer.start();
        log.info("starting java socket5 proxy server done..");
    }

    @PreDestroy
    public void destroy() {
        log.info("stop java socket5 proxy server...");
        if (null != socksProxyServer) {
            socksProxyServer.stop();
        }
        log.info("stop java socket5 proxy server done...");
    }
}
