package org.lotus.carp.webssh.navicat.config;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;
import com.github.monkeywie.proxyee.intercept.common.CertDownIntercept;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

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
public class WebSshHttpProxyServerConfig implements InitializingBean {
    HttpProxyServer httpProxyServer;
    @Resource
    WebSshConfig webSshConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpProxyServerConfig config = new HttpProxyServerConfig();
        //enable HTTPS support
        //If not enabled, HTTPS will not be intercepted, but forwarded directly to the raw packet.
        config.setHandleSsl(true);

        if(webSshConfig.isDebugHttpProxy()){
            log.info(String.format("config http proxy config with user:%s,password:%s"
                    ,webSshConfig.getHttpProxyUserName(),webSshConfig.getHttpProxyPassword()));
        }
        //set basic authenticate
        config.setAuthenticationProvider(new BasicHttpProxyAuthenticationProvider() {
            @Override
            protected BasicHttpToken authenticate(String usr, String pwd) {
                if(webSshConfig.isDebugHttpProxy()){
                    log.info(String.format("try authenticate with username:%s,password:%s",usr,pwd));
                }
                //set http proxy user info
                if (webSshConfig.getHttpProxyUserName().equals(usr) && webSshConfig.getHttpProxyPassword().equals(pwd)) {
                    return new BasicHttpToken(usr, pwd);
                }
                return null;
            }
        });
        log.info(String.format("starting http proxy server on ip:http://%s:%s"
                , null == webSshConfig.getHttpProxyBindIp() ? "localhost" : webSshConfig.getHttpProxyBindIp(), webSshConfig.getHttpProxyBIndPort()));
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
                .start(webSshConfig.getHttpProxyBindIp(), webSshConfig.getHttpProxyBIndPort());

        log.info("start http proxy server done.");
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
