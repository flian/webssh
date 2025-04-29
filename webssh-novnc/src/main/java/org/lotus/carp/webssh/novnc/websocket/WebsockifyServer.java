package org.lotus.carp.webssh.novnc.websocket;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author : foy
 * @date : 2025/4/25:14:10
 **/
@Slf4j
public class WebsockifyServer {
    Socket conn;
    private final WebSocketSession session;

    private static final int VNC_SOCKET_TIMEOUT = 3000;
    private static final ThreadPoolExecutor threadPool;
    static {
        threadPool = new ThreadPoolExecutor(5, 20,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        threadPool.allowCoreThreadTimeOut(true);
    }

    public boolean isServerOk(){
        return conn != null && session != null && conn.isConnected() && session.isOpen();
    }

    public WebsockifyServer(String targetHost, int targetPort,WebSocketSession session){
        this.session = session;
        conn = new Socket();
        //create connect
        try {
            conn.connect(new InetSocketAddress(targetHost, targetPort),VNC_SOCKET_TIMEOUT);
        } catch (IOException e) {
            log.error("create proxy socket connect to vnc server fail.host:{},port:{},timeout:{}",targetHost,targetPort,VNC_SOCKET_TIMEOUT,e);
            throw new WebSshBusinessException("create proxy socket connect to vnc server fail");
        }
        //new Thread(this::forwardVncToWebSocket).start();
        //submit forward
        threadPool.submit(this::forwardVncToWebSocket);
    }

    protected void safeCloseConnAndSessionOnException(WebSocketSession session){
        if(null == session){
            log.error("session is null");
            return;
        }

        if(null != conn && conn.isConnected()){
            try {
                conn.close();
            } catch (IOException e) {
                log.error("close socket fail.",e);
            }
        }

        try {
            session.close();
        } catch (IOException e) {
            log.error("close websocket session error.",e);
        }

    }


    public void forwardData(ByteBuffer data) {
        try {
            if (conn != null && conn.isConnected()) {
                conn.getOutputStream().write(data.array());
            }
        } catch (IOException e) {
            log.error("Error forwarding data to VNC", e);
            safeCloseConnAndSessionOnException(session);
        }
    }

    private void forwardVncToWebSocket() {
        try {
            InputStream input = conn.getInputStream();
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
        }
    }
}
