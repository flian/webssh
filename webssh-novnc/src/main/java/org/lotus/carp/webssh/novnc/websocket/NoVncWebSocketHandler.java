package org.lotus.carp.webssh.novnc.websocket;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : foy
 * @date : 2025/4/25:14:13
 **/

@Slf4j
@Component(value = "noVncWebSocketHandler")
public class NoVncWebSocketHandler extends TextWebSocketHandler {

    @Resource
    private WebSshConfig webSshConfig;

    //server info.
    private final Map<String, WebsockifyServer> workingWebsockifyServerMaps = new ConcurrentHashMap<>();

    //current host opened max port.
    private final Map<String, Integer> targetVncCurrentPort = new ConcurrentHashMap<>();

    protected WebsockifyServer ensureWebsockifyServer(WebSocketSession session) {
        return ensureWebsockifyServer(session, true);
    }

    public Integer getCurrentHost(String host) {
        return targetVncCurrentPort.get(host);
    }

    private WebsockifyServer ensureWebsockifyServer(WebSocketSession session, boolean createConnectIfNotPresent) {
        String token = (String) session.getAttributes().get(webSshConfig.getTokenName());
        String host = (String) session.getAttributes().get(WebSshWebSocketHandshakeInterceptor.NO_VNC_TARGET_HOST);
        if (ObjectUtils.isEmpty(host)) {
            log.error("can't find target noVNC host.");
            return null;
        }
        Integer port = Integer.parseInt((String)session.getAttributes().get(WebSshWebSocketHandshakeInterceptor.NO_VNC_TARGET_PORT));

        String serverKey = vncConnectServerKey(token, host, port,session);
        if (createConnectIfNotPresent && !workingWebsockifyServerMaps.containsKey(serverKey)) {
            WebsockifyServer websockifyServer = new WebsockifyServer(host, port);
            websockifyServer.addSession(session);
            targetVncCurrentPort.put(host, port);
            workingWebsockifyServerMaps.put(serverKey, websockifyServer);
        }
        return workingWebsockifyServerMaps.get(serverKey);
    }

    protected String vncConnectServerKey(String token, String host, Integer port,WebSocketSession session) {
        String preFix = token;
        if(ObjectUtils.isEmpty(token)){
            preFix = session.getId();
        }
        return String.format("%s_%s_%s", preFix, host, port);
    }


    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            WebsockifyServer server = ensureWebsockifyServer(session, false);
            // Forward binary data to VNC server
            if (null != server) {
                server.forwardData(session.getId(), message.getPayload());
            }
        } catch (Exception e) {
            log.error("Error forwarding WebSocket message", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("New noVNC connection: " + session.getId());
        ensureWebsockifyServer(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("noVNC connection closed: " + session.getId());
        WebsockifyServer server = ensureWebsockifyServer(session, false);
        if (null != server) {
            server.removeSession(session.getId());
        }
    }
}
