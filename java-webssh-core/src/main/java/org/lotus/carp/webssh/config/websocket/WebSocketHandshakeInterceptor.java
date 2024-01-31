/*
 * *
 *  * blog.coder4j.cn
 *  * Copyright (C) 2016-2019 All Rights Reserved.
 *
 */
package org.lotus.carp.webssh.config.websocket;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author buhao
 * @version MyInterceptor.java, v 0.1 2019-10-17 19:21 buhao
 */
@Component
@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Resource
    private WebSshLoginService webSshLoginService;

    /**
     * should verify token
     */
    @Value("${webSsh.shouldVerifyToken:true}")
    private boolean shouldVerifyToken;

    /**
     * token name from front
     */
    @Value("${webSsh.tokenName:token}")
    private String tokenName;

    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手开始");

        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), Charset.forName("utf-8"));
        String token = paramMap.get(tokenName);
        if (shouldVerifyToken) {
            if (!webSshLoginService.isTokenValid(token)) {
                log.info("token is invalid.token:{}", token);
                return false;
            }
        }
        if (!ObjectUtils.isEmpty(token)) {
            // 放入属性域
            attributes.put(tokenName, token);
            log.info("token verify success,handshake success.");
            return true;
        }

        if (!shouldVerifyToken) {
            log.info("ignore token verify.");
            return true;
        }
        log.info("用户登录已失效");
        return false;
    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成");
    }

}