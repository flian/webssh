package org.lotus.carp.webssh.novnc.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : foy
 * @date : 2025/4/25:14:10
 **/
@Slf4j
public class WebsockifyServer {
    private final String targetHost;
    private final int targetPort;
    private final Map<String, Socket> vncConnections = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> wsSessions = new ConcurrentHashMap<>();

    public WebsockifyServer(String targetHost, int targetPort) {
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    protected void safeCloseConnAndSessionOnException(WebSocketSession session){
        if(null == session){
            log.error("session is null");
            return;
        }
        String sessionId = session.getId();
        Socket conn = vncConnections.get(sessionId);
        if(null != conn && conn.isConnected()){
            try {
                conn.close();
            } catch (IOException e) {
                log.error("close socket fail.",e);
            }
        }
        vncConnections.remove(sessionId);
        wsSessions.remove(sessionId);
        try {
            session.close();
        } catch (IOException e) {
            log.error("close websocket session error.",e);
        }

    }

    public void addSession(WebSocketSession session) {
        wsSessions.put(session.getId(), session);
        try {
            Socket vncSocket = new Socket(targetHost, targetPort);
            vncConnections.put(session.getId(), vncSocket);

            // Start thread to forward VNC data to WebSocket
            new Thread(() -> forwardVncToWebSocket(session, vncSocket)).start();
        } catch (IOException e) {
            log.error("Error connecting to VNC server", e);
            safeCloseConnAndSessionOnException(session);

        }
    }

    public void removeSession(String sessionId) {
        try {
            Socket vncSocket = vncConnections.remove(sessionId);
            if (vncSocket != null) {
                vncSocket.close();
            }
            wsSessions.remove(sessionId);
        } catch (IOException e) {
            log.error("Error closing VNC connection", e);
            safeCloseConnAndSessionOnException(wsSessions.get(sessionId));
        }
    }

    public void forwardData(String sessionId, ByteBuffer data) {
        try {
            Socket vncSocket = vncConnections.get(sessionId);
            if (vncSocket != null && vncSocket.isConnected()) {
                vncSocket.getOutputStream().write(data.array());
            }
        } catch (IOException e) {
            log.error("Error forwarding data to VNC", e);
            safeCloseConnAndSessionOnException(wsSessions.get(sessionId));
        }
    }

    private void forwardVncToWebSocket(WebSocketSession session, Socket vncSocket) {
        try {
            InputStream input = vncSocket.getInputStream();
            int byteBufferSize = 8192;
            byte[] buffer = new byte[byteBufferSize];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                if (session.isOpen()) {
                    session.sendMessage(new BinaryMessage(buffer, 0, bytesRead, bytesRead < byteBufferSize));
                } else {
                    break;
                }
            }
            //send end.
            //session.sendMessage(new BinaryMessage(new byte[0]));
        } catch (IOException e) {
            log.error("Error in VNC to WebSocket forwarding", e);
            safeCloseConnAndSessionOnException(session);
        } finally {
            removeSession(session.getId());
        }
    }
}
