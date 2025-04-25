package org.lotus.carp.webssh.novnc.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author : foy
 * @date : 2025/4/25:14:13
 **/
@Slf4j
public class NoVncWebSocketHandler extends TextWebSocketHandler {


    private final WebsockifyServer websockifyServer;

    public NoVncWebSocketHandler() {
        // Configure your VNC server details
        String vncHost = "192.168.3.101";
        int vncPort = 5901;

        this.websockifyServer = new WebsockifyServer(vncHost, vncPort);
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            // Forward binary data to VNC server
            websockifyServer.forwardData(session.getId(), message.getPayload());
        } catch (Exception e) {
            log.error("Error forwarding WebSocket message", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("New noVNC connection: " + session.getId());

        websockifyServer.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("noVNC connection closed: " + session.getId());
        websockifyServer.removeSession(session.getId());
    }
}
