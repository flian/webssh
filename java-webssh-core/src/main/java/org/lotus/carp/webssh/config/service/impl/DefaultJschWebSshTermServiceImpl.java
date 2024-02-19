package org.lotus.carp.webssh.config.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
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


    //@see https://stackoverflow.com/questions/24623170/an-example-of-how-to-specify-terminal-modes-pty-req-string-for-ssh-client?rq=1
    private byte[] composeTerminalModes(){
        byte[] terminalModes = {
                (byte)0x35,                           //ECHO 53
                0,0,0,1,                             //1
                /*(byte)0x80,                       // TTY_OP_ISPEED 128
                0, 0, (byte)0x38, (byte)0x40,      // 14400 = 00003840
                (byte)0x81,                       // TTY_OP_OSPEED 129
                0, 0, (byte)0x38, (byte)0x40,    // 14400 again*/
                0,                              // TTY_OP_END
        };
        return terminalModes;
    }
    @Override
    public boolean initTermWebShhConnect(String sshInfo, WebSocketSession webSocketSession) throws IOException {
        try {
            CachedWebSocketSessionObject cachedObj = cachedObjMap.get(webSocketSession.getId());
            if (null == cachedObj) {
                SshInfo sshInfoObject = objectMapper.readValue(deCodeBase64Str(sshInfo), SshInfo.class);
                JSch.setLogger(new JschLogger());
                JSch jsch = new JSch();
                Hashtable<String, String> config = new Hashtable();
                config.put("StrictHostKeyChecking", "no");
                config.put("PreferredAuthentications", "password");
                jsch.setConfig(config);
                Session session = jsch.getSession(sshInfoObject.getUsername(), sshInfoObject.getIpaddress(), sshInfoObject.getPort());

                session.setPassword(sshInfoObject.getPassword());
                session.connect(30 * 1000);
                // seems need to set... try set model....
                Channel channel = session.openChannel("shell");
                //((ChannelShell)channel).setPtyType("xterm");
                ((ChannelShell)channel).setPty(true);
                // should set mode
                //SEE JschSshClient.createShell
                ((ChannelShell)channel).setTerminalMode(composeTerminalModes());
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
        //webSocketSession.sendMessage(message);
        //https://blog.csdn.net/xincang_/article/details/129054940
        //https://www.jianshu.com/p/db8a860b286c
        CachedWebSocketSessionObject cachedObj = cachedObjMap.get(webSocketSession.getId());
        //then send cmd to ssh term
        Channel channel = cachedObj.getSshChannel();
        StringBuffer sb = cachedObj.getCommand();
        String msgGet = message.getPayload();
        PrintWriter printWriter = new PrintWriter(channel.getOutputStream());
        printWriter.write(msgGet);
        printWriter.flush();
        //cache cmd
        /*sb.append(msgGet);
        if (sb.length() > 10000) {
            //close it.
            webSocketSession.close();
        }
        if ("\r".equals(msgGet) || "\n".equals(msgGet) || "\r\n".equals(msgGet)) {
            String cmd = sb.toString();
            sb.delete(0, sb.length());
            printWriter.write(cmd);
            printWriter.flush();
        }*/
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
