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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
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
        threadPool.submit(() -> {
            try {
                InputStream inputStreamReader = channel.getInputStream();
                //循环读取
                byte[] buffer = new byte[1024];
                //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while (inputStreamReader.read(buffer) != -1) {
                    webSocketSession.sendMessage(new TextMessage(buffer));
                }
            } catch (IOException e) {
                log.error("error while send term message back to websocket.", e);
            }
        });
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
                Hashtable<String, String> config = new Hashtable();
                config.put("StrictHostKeyChecking", "no");
                config.put("PreferredAuthentications", "password");
                jsch.setConfig(config);
                Session session = jsch.getSession(sshInfoObject.getUsername(), sshInfoObject.getIpaddress(), sshInfoObject.getPort());

                session.setPassword(sshInfoObject.getPassword());
                session.connect(30 * 1000);
                Channel channel = session.openChannel("shell");
                channel.connect(30 * 1000);
                cachedObj = new CachedWebSocketSessionObject();
                cachedObj.setSshInfo(sshInfoObject);
                cachedObj.setSshChannel(channel);
                cachedObj.setSshSession(session);
                cachedObjMap.put(webSocketSession.getId(), cachedObj);
                sendSshMessageBack(webSocketSession, channel);
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
        //send message back
        webSocketSession.sendMessage(message);

        CachedWebSocketSessionObject cachedObj = cachedObjMap.get(webSocketSession.getId());
        //then send cmd to ssh term
        Channel channel = cachedObj.getSshChannel();
        StringBuffer sb = cachedObj.getCommand();
        String msgGet = message.getPayload();
        PrintWriter printWriter = new PrintWriter(channel.getOutputStream());
        //cache cmd
        sb.append(msgGet);
        if (sb.length() > 10000) {
            //close it.
            webSocketSession.close();
        }
        if ("\r".equals(msgGet) || "\n".equals(msgGet) || "\r\n".equals(msgGet)) {
            String cmd = sb.toString();
            sb.delete(0, sb.length());
            printWriter.write(cmd);
            printWriter.flush();
        }
        return true;
    }

    @Override
    public boolean onSessionClose(WebSocketSession webSocketSession) {
        if (cachedObjMap.containsKey(webSocketSession.getId())) {
            cachedObjMap.get(webSocketSession.getId()).close();
            cachedObjMap.remove(webSocketSession.getId());
        }
        return true;
    }
}
