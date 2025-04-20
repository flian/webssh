package org.lotus.carp.webssh.proxy.config;

import lombok.extern.slf4j.Slf4j;
import org.bbottema.javasocksproxyserver.SocksServer;
import org.bbottema.javasocksproxyserver.auth.UsernamePasswordAuthenticator;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author : foy
 * @date : 2025/4/15:16:30
 **/

@Component
@Slf4j
public class WebSshSocketProxyServerComponent implements InitializingBean {
    protected SocksServer socksProxyServer;
    @Resource
    protected WebSshConfig webSshConfig;

    private transient boolean isStarted = false;

    private String proxyUserName;
    private String proxyPassword;

    private String proxyBindIp;
    private int proxyBindPort;

    public String getPort() {
        return "" + webSshConfig.getSocketProxyPort();
    }

    public boolean stopProxyServer(){
        log.info("try stop proxy sever.");
        destroy();
        log.info("stop proxy sever done.");
        return isServerStarted();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        proxyUserName = webSshConfig.getSocketProxyUserName();
        proxyPassword = webSshConfig.getSocketProxyPassword();
        proxyBindIp = webSshConfig.getSocketBindIp();
        proxyBindPort = webSshConfig.getSocketProxyPort();

        if(webSshConfig.isStartProxyOnStartup()){
            log.info("try start proxy server by startup.result:{}",this.startProxyServer());
        }
    }
    public boolean isServerStarted(){
        return this.isStarted;
    }
    public boolean startProxyServer(){
        if (!webSshConfig.isEnableSocketProxy()) {
            log.info("enableSocketProxy is false. server will not start socket proxy.");
            return isServerStarted();
        }
        if(isServerStarted()){
            log.warn("proxy server is running...,no need start again.");
            return isServerStarted();
        }
        synchronized (this){
            if(isServerStarted()){
                log.warn("proxy server is running...,no need start again.");
                return isServerStarted();
            }
            startServerInternal();
            isStarted = true;
        }
        return isServerStarted();
    }
    protected void startServerInternal(){

        log.info(String.format("starting java socket5 proxy server on port:%s..., enabled NO_AUTH:%s", proxyBindPort,webSshConfig.isSocketNoAuthProxy()));
        if (webSshConfig.isDebugHttpProxy()) {
            log.info(String.format("socket5 proxy username:%s,password:%s"
                    , proxyUserName, proxyPassword));
        }

        socksProxyServer = new SocksServer(proxyBindPort).setAuthenticator(new UsernamePasswordAuthenticator(webSshConfig.isSocketNoAuthProxy()) {
            @Override
            public boolean validate(String username, String password) {
                return proxyUserName.equals(username)
                        && password.equals(proxyPassword);
            }
        });

        if(!ObjectUtils.isEmpty(proxyBindIp)){
            log.info("bind socket listen ip:{}",proxyBindIp);
            socksProxyServer.setFactory(new ServerSocketFactory(){
                @Override
                public ServerSocket createServerSocket(int port) throws IOException {
                    //bind socket ip if config
                    return new ServerSocket(port,50,InetAddress.getByName(proxyBindIp));
                }

                @Override
                public ServerSocket createServerSocket(int port, int backlog) throws IOException {
                    return new ServerSocket(port,backlog);
                }

                @Override
                public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
                    return new ServerSocket(port,backlog,bindAddr);
                }
            });
        }
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
