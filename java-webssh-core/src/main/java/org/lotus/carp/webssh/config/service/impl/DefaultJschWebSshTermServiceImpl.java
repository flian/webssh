package org.lotus.carp.webssh.config.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.WebSshTermService;
import org.lotus.carp.webssh.config.service.impl.vo.CachedWebSocketSessionObject;
import org.lotus.carp.webssh.config.service.vo.SshInfo;
import org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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


    private static final int DEFAULT_COL = 80;
    private static final int DEFAULT_ROW = 24;
    private static final int DEFAULT_WP = 640;
    private static final int DEFAULT_HP = 480;

    private int getCol(WebSocketSession webSocketSession) {
        String tmp = (String) webSocketSession.getAttributes().get(WebSshWebSocketHandshakeInterceptor.COLS);
        if (ObjectUtils.isEmpty(tmp)) {
            return DEFAULT_COL;
        }
        return Integer.parseInt(tmp);
    }

    private int getRow(WebSocketSession webSocketSession) {
        String tmp = (String) webSocketSession.getAttributes().get(WebSshWebSocketHandshakeInterceptor.ROWS);
        if (ObjectUtils.isEmpty(tmp)) {
            return DEFAULT_ROW;
        }
        return Integer.parseInt(tmp);
    }

    private int getWp(WebSocketSession webSocketSession) {
        return DEFAULT_WP;
    }

    private int getHp(WebSocketSession webSocketSession) {
        return DEFAULT_HP;
    }

    public void sendSshMessageBack(WebSocketSession webSocketSession, Channel channel) {
        threadPool.submit(() -> {
            try {
                InputStream inputStreamReader = cachedObjMap.get(webSocketSession.getId()).getChannelInputStream();
                ;
                //循环读取
                byte[] buffer = new byte[1024];
                //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while (inputStreamReader.read(buffer) != -1) {
                    webSocketSession.sendMessage(new TextMessage(buffer));
                    buffer = new byte[1024];
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
    private byte[] composeTerminalModes() {
        //can see "tail -f /var/log/secure" in your linux server for more login error detail.
        byte[] terminalModes = {
                //Translate uppercase characters to lowercase.
                37,
                0, 0, 0, 0,
                //ECHO 53
                53,
                0, 0, 0, 1,
                //ECHOE Visually erase chars.
                54,
                0, 0, 0, 0,
                //ECHOK Kill character discards current line.
                55,
                0, 0, 0, 0,
                //ECHONL Echo NL even if ECHO is off
                56,
                0, 0, 0, 0,
                //ECHOCTL Echo control characters as ^(Char).
                60,
                0, 0, 0, 0,
                // 1,
                // TTY_OP_ISPEED 128
                (byte) 0x80,
                // 14400 = 00003840
                0, 0, (byte) 0x38, (byte) 0x40,
                // TTY_OP_OSPEED 129
                (byte) 0x81,
                // 14400 again
                0, 0, (byte) 0x38, (byte) 0x40,
                // TTY_OP_END
                0,
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
                ((ChannelShell) channel).setPtyType("xterm");
                ((ChannelShell) channel).setPtySize(getCol(webSocketSession), getRow(webSocketSession), getWp(webSocketSession), getHp(webSocketSession));
                ((ChannelShell) channel).setPty(true);

                // should set mode
                ((ChannelShell) channel).setTerminalMode(composeTerminalModes());
                cachedObj = new CachedWebSocketSessionObject();
                cachedObj.setChannelInputStream(channel.getInputStream());
                channel.connect(30 * 1000);
                cachedObj.setChannelOutputStream(channel.getOutputStream());

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
        String msgGet = message.getPayload();
        OutputStream outputStream = cachedObjMap.get(webSocketSession.getId()).getChannelOutputStream();

        //write cmd to jsch
        outputStream.write(msgGet.getBytes());
        outputStream.flush();

        return true;
    }

    @Override
    public boolean handleTermWebSShResize(WebSocketSession webSocketSession, TextMessage message, int rows, int cols) {
        int row = rows > 0 ? rows : DEFAULT_ROW;
        int col = cols > 0 ? cols : DEFAULT_COL;
        int wp = row * 8;
        int hp = col * 20;
        log.info("handler resize,row:{},col;{},wp:{},hp:{}", row, col, wp, hp);
        CachedWebSocketSessionObject cachedObj = cachedObjMap.get(webSocketSession.getId());
        //then send cmd to ssh term
        Channel channel = cachedObj.getSshChannel();
        ((ChannelShell) channel).setPtySize(row, col, wp, hp);
        log.info("handler resize success!");
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
