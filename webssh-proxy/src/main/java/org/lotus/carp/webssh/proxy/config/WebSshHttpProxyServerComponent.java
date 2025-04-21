package org.lotus.carp.webssh.proxy.config;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;
import com.github.monkeywie.proxyee.intercept.common.FullResponseIntercept;
import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;
import com.github.monkeywie.proxyee.server.auth.BasicHttpProxyAuthenticationProvider;
import com.github.monkeywie.proxyee.server.auth.model.BasicHttpToken;
import com.github.monkeywie.proxyee.util.HttpUtil;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.proxy.controller.vo.ProxyOpRequestVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author : foy
 * @date : 2025/4/16:09:46
 * http proxy setting for webssh
 **/
@Component
@Slf4j
public class WebSshHttpProxyServerComponent implements InitializingBean {
    HttpProxyServer httpProxyServer;
    @Resource
    WebSshConfig webSshConfig;

    private transient boolean isStarted = false;

    private String proxyUserName;
    private String proxyPassword;

    private String proxyBindIp;
    private int proxyBindPort;

    public String proxyPort(){
        return ""+proxyBindPort;
    }

    public boolean updateProxy(ProxyOpRequestVo requestVo){
        if(1 != requestVo.getProxyType()){
            log.info("not this type.ignore.");
            return Boolean.FALSE;
        }
        tryReConfigProperties(requestVo);
        if(-1 == requestVo.getOp()){
            stopProxyServer();
            startProxyServer();
        }else if(0 == requestVo.getOp()){
            startProxyServer();
        }else if(1 == requestVo.getOp()){
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

    public boolean isServerStarted(){
        return this.isStarted;
    }

    public boolean startProxyServer(){
        if(!webSshConfig.isEnableHttpProxy()){
            log.info("EnableHttpProxy is false. server will not start http proxy.");
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
        HttpProxyServerConfig config = new HttpProxyServerConfig();
        //enable HTTPS support
        //If not enabled, HTTPS will not be intercepted, but forwarded directly to the raw packet.
        config.setHandleSsl(true);

        if(webSshConfig.isDebugHttpProxy()){
            log.info(String.format("config http proxy config with user:%s,password:%s"
                    ,proxyUserName,proxyPassword));
        }
        //set basic authenticate
        config.setAuthenticationProvider(new BasicHttpProxyAuthenticationProvider() {
            @Override
            protected BasicHttpToken authenticate(String usr, String pwd) {
                if(webSshConfig.isDebugHttpProxy()){
                    log.info(String.format("try authenticate with username:%s,password:%s",usr,pwd));
                }
                if(webSshConfig.isHttpProxyNoAuth()){
                    return new BasicHttpToken("","");
                }
                //set http proxy user info
                if (proxyUserName.equals(usr) && proxyPassword.equals(pwd)) {
                    return new BasicHttpToken(usr, pwd);
                }
                return null;
            }
        });
        log.info(String.format("starting http proxy server on ip:http://%s:%s"
                , null == proxyBindIp ? "localhost" : proxyBindIp, proxyBindPort));
        new HttpProxyServer()
                .serverConfig(config)
                .proxyInterceptInitializer(new HttpProxyInterceptInitializer() {
                    @Override
                    public void init(HttpProxyInterceptPipeline pipeline) {
                        //cert FIXME process ca cert download
                        //pipeline.addFirst(new CertDownIntercept());
                        //is debug
                        if (webSshConfig.isDebugHttpProxy()) {
                            pipeline.addLast(new FullResponseIntercept() {

                                @Override
                                public boolean match(HttpRequest httpRequest, HttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
                                    //Insert js when matching to Baidu homepage
                                    return (debugCheck(pipeline.getHttpRequest()) || HttpUtil.checkUrl(pipeline.getHttpRequest(), "^www.baidu.com$"))
                                            && HttpUtil.isHtml(httpRequest, httpResponse);
                                }

                                @Override
                                public void handleResponse(HttpRequest httpRequest, FullHttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
                                    //Print raw packet
                                    log.info(httpResponse.toString());
                                    log.info(httpResponse.content().toString(Charset.defaultCharset()));
                                    //Edit response header and response body
                                    httpResponse.headers().set("handel", "edit head");
                                    httpResponse.content().writeBytes("<script>alert('hello proxyee')</script>".getBytes());
                                }
                            });
                        }
                    }

                })
                .startAsync(proxyBindIp, proxyBindPort);

        log.info("start http proxy server done.");

    }
    public boolean stopProxyServer(){
        log.info("try stop proxy sever.");
        if(isServerStarted()){
            destroy();
        }
        log.info("stop proxy sever done.");
        return isServerStarted();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(webSshConfig.isStartProxyOnStartup()){
            log.info("try start proxy server by startup.result:{}",this.startProxyServer());
        }
    }

    private void initProperties(){
        this.proxyUserName = webSshConfig.getHttpProxyUserName();
        this.proxyPassword = webSshConfig.getHttpProxyPassword();
        this.proxyBindIp = webSshConfig.getHttpProxyBindIp();
        this.proxyBindPort = webSshConfig.getHttpProxyBindPort();
    }
    private boolean debugCheck(HttpRequest request){
        //just for debug
        if(HttpUtil.checkUrl(request,"^www.moe.gov.cn/jyb_xwfb/gzdt_gzdt/moe_1485/202504/t20250415_1187419.html$")){
            return true;
        }
        if(HttpUtil.checkUrl(request,"^webssh.http.proxy.test.*$"));
        return false;
    }
    @PreDestroy
    public void destroy() {
        log.info("stop http proxy server...");
        if (null != httpProxyServer) {
            httpProxyServer.close();
        }
        log.info("stop http proxy server done...");
    }
}
