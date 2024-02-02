/*
 * *
 *  * blog.coder4j.cn
 *  * Copyright (C) 2016-2019 All Rights Reserved.
 *
 */
package org.lotus.carp.webssh.config.websocket;


import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

import static org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor.CMD;

/**
 * @author buhao
 * @version MyWSHandler.java, v 0.1 2019-10-17 17:10 buhao
 */
@Component
@Slf4j
public class WebSshWebsocketHandler extends TextWebSocketHandler {


    @Resource
    private WebSshConfig webSshConfig;

    /**
     * socket 建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        Object token = session.getAttributes().get(webSshConfig.getTokenName());
        if (sessionId != null && token != null) {
            WebSshWsSessionManager.add(sessionId, session);
        } else {
            if (!webSshConfig.isShouldVerifyToken()) {
                log.info("skip token verify");
                return;
            }
            throw new RuntimeException("user not exist,please reLogin!");
        }
    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSshUrlCommandEnum cmd = WebSshUrlCommandEnum.getByCode((String) session.getAttributes().get(CMD));
        if (null == cmd) {
            log.info("unknown command...");
        }
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object token = session.getAttributes().get(webSshConfig.getTokenName());
        log.info("server 接收到 " + token + " 发送的 " + payload);
        switch (cmd) {
            case TERM: {
                //term command get
                break;
            }
            case FILE_UPLOAD_PROGRESS: {
                //file upload
                break;
            }
        }
        //session.sendMessage(message);
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object token = session.getAttributes().get(webSshConfig.getTokenName());
        String sessionId = session.getId();
        if (sessionId != null && token != null) {
            // 用户退出，移除缓存
            WebSshWsSessionManager.remove(sessionId);
        }
    }


}