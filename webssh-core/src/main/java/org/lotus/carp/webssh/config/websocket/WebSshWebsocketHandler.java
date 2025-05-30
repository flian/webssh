
package org.lotus.carp.webssh.config.websocket;


import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.config.service.WebSshTermService;
import org.lotus.carp.webssh.config.service.impl.JschSftpUploadProcessMonitor;
import org.lotus.carp.webssh.config.utils.WebSshConstants;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.config.websocket.websshenum.WebSshUrlCommandEnum;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;

import static org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor.*;


/**
 * <h3>WebSshWebsocketHandler</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 14:49
 **/

@Slf4j
public class WebSshWebsocketHandler extends TextWebSocketHandler {


    @Resource
    private WebSshConfig webSshConfig;

    @Resource
    private WebSshTermService webSshTermService;

    @Resource
    private WebSshLoginService webSshLoginService;

    /**
     * socket 建立成功事件
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        WebSshUrlCommandEnum cmd = WebSshUrlCommandEnum.getByCode((String) session.getAttributes().get(CMD));
        String sessionId = session.getId();
        Object token = session.getAttributes().get(webSshConfig.getTokenName());

        if (ObjectUtils.isEmpty(sessionId)) {
            session.close();
            throw new WebSshBusinessException("sessionId should not be empty.");
        }
        if (ObjectUtils.isEmpty(token)) {
            if (webSshConfig.isShouldVerifyToken()) {
                session.close();
                throw new WebSshBusinessException("token is required!.");
            }
        }
        if (webSshConfig.isShouldVerifyToken()) {
            if (!webSshLoginService.isTokenValid((String) token)) {
                session.close();
                throw new WebSshBusinessException("token is invalid!.");
            }
        }
        if (null != cmd) {
            if (WebSshUrlCommandEnum.TERM.equals(cmd)) {
                //current only add TERM session for future use.
                WebSshWsSessionManager.add(sessionId, session);
            }
        }

        switch (cmd) {
            case TERM: {
                //term websocket connection should connect to jsch
                String sshInfo = (String) session.getAttributes().get(SSH_INFO);
                if (ObjectUtils.isEmpty(sshInfo)) {
                    session.sendMessage(new TextMessage("ssh connection info error, empty sshinfo."));
                    session.close();
                }
                if (!webSshTermService.initTermWebSshConnect(sshInfo, session)) {
                    //connect error,close session.
                    session.close();
                }
                break;
            }
            case FILE_UPLOAD_PROGRESS: {
                //file upload
                //process file upload message.
                String fileUid = (String) session.getAttributes().get(ID);
                int maxCnt = webSshConfig.getCloseWebSocketBeforeCheckCount();
                int checkCnt = 0;
                if (ObjectUtils.isEmpty(fileUid)) {
                    //file id is null. not a valid websocket connection.
                    session.close();
                }
                while (true) {
                    boolean isFileUploading = JschSftpUploadProcessMonitor.isFileUploading(fileUid);
                    if (isFileUploading) {
                        //file is uploading. send uploaded size back.
                        session.sendMessage(new TextMessage("" + JschSftpUploadProcessMonitor.uploadedSize(fileUid)));
                    } else {
                        //file upload is not start or is finished.
                        //wait maxCnt (3) iterator times.
                        if (checkCnt >= maxCnt) {
                            break;
                        }
                        checkCnt++;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        log.error("FILE_UPLOAD_PROGRESS error", e);
                        break;
                    }
                }
                //upload process done,close session.
                session.close();
                break;
            }

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
        log.info("server received " + token + " send  " + payload);
        switch (cmd) {
            case TERM: {
                //term command get
                //ping
                if (WebSshConstants.PING_MSG.equalsIgnoreCase(message.getPayload())) {
                    //ping,should ignore
                    break;
                }
                //resize
                if (message.getPayload().contains(WebSshConstants.RESIZE_MSG)) {
                    String[] temps = message.getPayload().split(":");
                    log.info("resize term,rows:{},cols:{}",Integer.parseInt(temps[1]), Integer.parseInt(temps[2]));
                    webSshTermService.handleTermWebSShResize(session, message,
                            Integer.parseInt(temps[1]), Integer.parseInt(temps[2]));
                    break;
                }
                //session.sendMessage(message);
                webSshTermService.handleTermWebSshMsg(session, message);
                break;
            }
            case FILE_UPLOAD_PROGRESS: {
                break;
            }
        }
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
        webSshTermService.onSessionClose(session);

    }


}