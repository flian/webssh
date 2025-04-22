package org.lotus.carp.webssh.proxy.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bbottema.javasocksproxyserver.SocksServer;
import org.bbottema.javasocksproxyserver.auth.UsernamePasswordAuthenticator;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.proxy.controller.vo.ProxyOpRequestVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

/**
 * @author : foy
 * @date : 2025/4/15:16:30
 **/

@Component
@Slf4j
public class WebSshSocketProxyServerComponent implements InitializingBean {
    protected SocksServer socksProxyServer;

    Cache<String,String> socketProxyCache;

    @Resource
    protected WebSshConfig webSshConfig;

    private transient boolean isStarted = false;

    @Getter
    private String proxyUserName;
    @Getter
    private String proxyPassword;

    private String proxyBindIp;
    private int proxyBindPort;

    private int autoStopIn;
    public String getPort() {
        return "" + webSshConfig.getSocketProxyPort();
    }

    public boolean stopProxyServer(){
        log.info("try stop proxy sever.");
        if(isServerStarted()){
            socketProxyCache.invalidate(proxyCacheName());
            destroy();
        }
        log.info("stop proxy sever done.");
        return isServerStarted();
    }

    public boolean updateProxy(ProxyOpRequestVo requestVo){
        if(WebSshProxyConstants.SOCKET_PROXY_TYPE != requestVo.getProxyType()){
            log.info("not this type.ignore.");
            return Boolean.FALSE;
        }
        tryReConfigProperties(requestVo);
        if(WebSshProxyConstants.OP_RESTART == requestVo.getOp()){
            stopProxyServer();
            startProxyServer();
        }else if(WebSshProxyConstants.OP_START == requestVo.getOp()){
            startProxyServer();
        }else if(WebSshProxyConstants.OP_STOP == requestVo.getOp()){
            stopProxyServer();
        }
        return Boolean.TRUE;

    }

    private void tryReConfigProperties(ProxyOpRequestVo requestVo){
        if(!ObjectUtils.isEmpty(requestVo.getBindIp())){
            this.proxyBindIp = requestVo.getBindIp();
        }
        if(!ObjectUtils.isEmpty(requestVo.getBindPort())){
            this.proxyBindPort = Integer.parseInt(requestVo.getBindPort());
        }

        if(!ObjectUtils.isEmpty(requestVo.getUsername())){
            this.proxyUserName = requestVo.getUsername();
        }

        if(!ObjectUtils.isEmpty(requestVo.getPassword())){
            this.proxyPassword = requestVo.getPassword();
        }
    }

    String proxyCacheName(){
       return WebSshHttpProxyServerComponent.class.getName();
    }
    void setUpCacheEvent(){
        String name = proxyCacheName();
        if(!ObjectUtils.isEmpty(socketProxyCache)){
            socketProxyCache.invalidateAll();
            socketProxyCache.cleanUp();
        }
        socketProxyCache = CacheBuilder.newBuilder().maximumSize(2).expireAfterAccess(autoStopIn, TimeUnit.HOURS)
                .removalListener(notification->{
                    if(notification.wasEvicted()){
                        log.info("process Evicted proxy cache event.{}",notification);
                        this.stopProxyServer();
                        log.info("process Evicted proxy cache event done.{}",notification);
                    }
                }).build();
        socketProxyCache.put(name,""+System.currentTimeMillis());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        proxyUserName = webSshConfig.getSocketProxyUserName();
        proxyPassword = webSshConfig.getSocketProxyPassword();
        proxyBindIp = webSshConfig.getSocketBindIp();
        proxyBindPort = webSshConfig.getSocketProxyPort();
        autoStopIn = webSshConfig.getTokenExpiration();
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
        setUpCacheEvent();
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
