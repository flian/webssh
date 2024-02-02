package org.lotus.carp.webssh.config.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.WebSshTermService;
import org.lotus.carp.webssh.config.service.impl.vo.CachedWebSocketSessionObject;
import org.lotus.carp.webssh.config.service.vo.SshInfo;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-02 15:28
 **/

@Slf4j
public class DefaultJschWebSshTermServiceImpl implements WebSshTermService {
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 100, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10000));
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, CachedWebSocketSessionObject> cachedObjMap = new ConcurrentHashMap<>();

    public void sendSshMessageBack(WebSocketSession webSocketSession, Channel channel) {
        try {
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(channel.getInputStream());
            BufferedReader input = new BufferedReader(inputStreamReader);
            String line;
            while ((line = input.readLine()) != null) {
                webSocketSession.sendMessage(new TextMessage(line));
            }
        } catch (IOException e) {
            log.error("error while send term message back to websocket.", e);
        }
    }

    private String deCodeBase64Str(String sshInfo) {
        return Base64Decoder.decodeStr(sshInfo);
    }

    @Override
    public boolean initTermWebShhConnect(String sshInfo, WebSocketSession webSocketSession) throws IOException {
        try {
            CachedWebSocketSessionObject cachedObj = cachedObjMap.get(webSocketSession.getId());
            if (null == cachedObj) {
                SshInfo sshInfoObject = objectMapper.readValue(deCodeBase64Str(sshInfo), SshInfo.class);
                JSch jsch = new JSch();
                Session session = jsch.getSession(sshInfoObject.getUsername(), sshInfoObject.getIpaddress(), sshInfoObject.getPort());
                session.setPassword(sshInfoObject.getPassword());
                Channel channel = session.openChannel("shell");
                channel.connect(3 * 1000);
                cachedObj = new CachedWebSocketSessionObject();
                cachedObj.setSshInfo(sshInfoObject);
                cachedObjMap.put(webSocketSession.getId(), cachedObj);
                threadPool.submit(() -> {
                    sendSshMessageBack(webSocketSession, channel);
                });
            }
            return true;
        } catch (JsonProcessingException e) {
            log.error("error parse sshInfo object。sshInfo:{},exception:{}", sshInfo, e);
            webSocketSession.sendMessage(new TextMessage(e.getMessage()));
        } catch (JSchException e) {
            log.error("error login to jsch.sshInfo:{},exception:{}", sshInfo, e);
            webSocketSession.sendMessage(new TextMessage(e.getMessage()));
        } catch (Exception e) {
            log.error("error when init ssh connection to server.sshInfo:{},exception:{}", sshInfo, e);
            webSocketSession.sendMessage(new TextMessage(e.getMessage()));
        }
        return false;
    }

    @Override
    public boolean handleTermWebSshMsg(WebSocketSession webSocketSession, TextMessage message) throws IOException {
        Channel channel = cachedObjMap.get(webSocketSession.getId()).getSshChannel();
        PrintWriter printWriter = new PrintWriter(channel.getOutputStream());
        printWriter.write(message.getPayload());
        //sendSshMessageBack(webSocketSession, channel);
        return true;
    }

    @Override
    public boolean onSessionClose(WebSocketSession webSocketSession) {
        cachedObjMap.get(webSocketSession.getId()).close();
        return false;
    }
}
