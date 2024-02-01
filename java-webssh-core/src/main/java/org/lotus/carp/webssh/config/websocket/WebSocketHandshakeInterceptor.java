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
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author buhao
 * @version MyInterceptor.java, v 0.1 2019-10-17 19:21 buhao
 */
@Component
@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    public static final String CMD = "CMD";

    public static final String ID = "id";

    public static final String SSH_INFO = "sshInfo";

    public static final String ROWS = "rows";
    public static final String COLS = "cols";
    public static final String CLOSE_TIP = "closeTip";


    @Resource
    private WebSshLoginService webSshLoginService;

    @Resource
    private WebSshConfig webSshConfig;



    public void setSessionParamIfPresent(String requestUri, Map<String, String> paramMap, Map<String, Object> attributes) {
        String cmd = WebSshUrlCommandEnum.getCmdByUri(requestUri);
        if (ObjectUtils.isEmpty(cmd)) {
            log.info("can't find cmd, is request not right? requestUri：{}", requestUri);
        }
        attributes.put(CMD, cmd);
        attributes.put(SSH_INFO, paramMap.get(SSH_INFO));
        attributes.put(ID, paramMap.get(ID));
        attributes.put(ROWS, paramMap.get(ROWS));
        attributes.put(COLS, paramMap.get(COLS));
        attributes.put(CLOSE_TIP, paramMap.get(CLOSE_TIP));
    }

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
        if (!request.getURI().getPath().contains(webSshConfig.getWebSshUri())) {
            log.info("not webssh websocket.. contine..");
            return true;
        }

        log.info("握手开始");
        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), Charset.forName("utf-8"));
        //set connection config.
        setSessionParamIfPresent(request.getURI().getPath(), paramMap, attributes);
        String token = paramMap.get(webSshConfig.getTokenName());
        if (webSshConfig.isShouldVerifyToken()) {
            if (!webSshLoginService.isTokenValid(token)) {
                log.info("token is invalid.token:{}", token);
                return false;
            }
        }
        if (!ObjectUtils.isEmpty(token)) {
            // 放入属性域
            attributes.put(webSshConfig.getTokenName(), token);
            log.info("token verify success,handshake success.");
            return true;
        }

        if (!webSshConfig.isShouldVerifyToken()) {
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