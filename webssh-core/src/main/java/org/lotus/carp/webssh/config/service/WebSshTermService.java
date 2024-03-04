package org.lotus.carp.webssh.config.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-02 15:27
 **/
public interface WebSshTermService {
    /**
     * init term websocket connection
     * @param sshInfo
     * @param webSocketSession
     * @return
     */
    boolean initTermWebSshConnect(String sshInfo, WebSocketSession webSocketSession) throws IOException;

    /**
     * handle front message
     * @param webSocketSession
     * @param message
     * @return
     */
    boolean handleTermWebSshMsg(WebSocketSession webSocketSession, TextMessage message) throws IOException;

    /**
     * handle resize
     * @param webSocketSession
     * @param message
     * @param rows
     * @param cols
     * @return
     */
    boolean handleTermWebSShResize(WebSocketSession webSocketSession, TextMessage message,int rows, int cols);

    /**
     * should invoke when session close
     * @param webSocketSession
     * @return
     */
    boolean onSessionClose(WebSocketSession webSocketSession);
}
