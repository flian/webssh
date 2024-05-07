package org.lotus.carp.webssh.config.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import net.propero.rdp.Rdesktop;
import net.propero.rdp.RdesktopException;
import org.lotus.carp.webssh.config.service.WebSshJavaRdpService;
import org.lotus.carp.webssh.config.service.WebSshTermService;
import org.lotus.carp.webssh.config.service.impl.vo.CachedWebSocketSessionObject;
import org.lotus.carp.webssh.config.service.vo.SshInfo;
import org.lotus.carp.webssh.config.websocket.WebSshWebSocketHandshakeInterceptor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-02 15:28
 **/

@Slf4j
public class DefaultJschWebSshTermServiceImpl extends JschBase implements WebSshTermService {


    private static ThreadPoolExecutor threadPool;
    private Map<String, CachedWebSocketSessionObject> xTermCachedObjMap = new ConcurrentHashMap<>();

    private int maxSessionCnt;

    private AtomicInteger currentSessionCnt = new AtomicInteger(0);

    @Resource
    private WebSshJavaRdpService webSshJavaRdpService;

    @Override
    public void subInit() {
        //set core poolSize = maxPoolSize
        threadPool = new ThreadPoolExecutor(webSshConfig.getMaxSshShellTermCorePoolSize(), webSshConfig.getMaxSshShellTermCorePoolSize(),
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        //and allow core thread time out,as this threadPool is running jsch outPutStream to front
        //it will block on waiting message or session close.
        //actually may direct create thread is better. or java 21 virtual thread.
        threadPool.allowCoreThreadTimeOut(true);
        maxSessionCnt = webSshConfig.getMaxSshShellTermCorePoolSize();
    }

    private int getCol(WebSocketSession webSocketSession) {
        String tmp = (String) webSocketSession.getAttributes().get(WebSshWebSocketHandshakeInterceptor.COLS);
        if (ObjectUtils.isEmpty(tmp)) {
            return webSshConfig.getWebSshTermCol();
        }
        return Integer.parseInt(tmp);
    }

    private int getRow(WebSocketSession webSocketSession) {
        String tmp = (String) webSocketSession.getAttributes().get(WebSshWebSocketHandshakeInterceptor.ROWS);
        if (ObjectUtils.isEmpty(tmp)) {
            return webSshConfig.getWebSshTermRow();
        }
        return Integer.parseInt(tmp);
    }

    private int getWp(WebSocketSession webSocketSession) {
        return webSshConfig.getWebSshTermWp();
    }

    private int getHp(WebSocketSession webSocketSession) {
        return webSshConfig.getWebSshTermHp();
    }

    /**
     * compose xTermShell size from websocket.
     *
     * @param webSocketSession
     * @return result[0]  col – terminal width
     * result[1]  columns row – terminal height
     * result[2]  rows wp – terminal width, pixels
     * result[3]  hp – terminal height, pixels
     */
    public int[] xTermShellSize(WebSocketSession webSocketSession) {
        int[] result = new int[4];
        result[0] = getCol(webSocketSession);
        result[1] = getRow(webSocketSession);
        int wp = result[0] * wpColRate();
        result[2] = wp > 0 ? wp : getWp(webSocketSession);
        int hp = result[1] * hpRowRate();
        result[3] = hp > 0 ? hp : getHp(webSocketSession);
        return result;
    }

    public void sendSshMessageBack(WebSocketSession webSocketSession, Channel channel) {
        threadPool.submit(() -> {
            try {
                //限制任务数
                currentSessionCnt.incrementAndGet();
                InputStream inputStreamReader = xTermCachedObjMap.get(webSocketSession.getId()).getChannelInputStream();
                //循环读取
                byte[] buffer = new byte[1024];
                //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while (inputStreamReader.read(buffer) != -1) {
                    webSocketSession.sendMessage(new TextMessage(buffer));
                    buffer = new byte[1024];
                }
            } catch (IOException e) {
                log.error("error while send term message back to websocket.", e);
            } finally {
                currentSessionCnt.decrementAndGet();
            }
        });
    }

    @Override
    public boolean initTermWebSshConnect(String sshInfo, WebSocketSession webSocketSession) throws IOException {
        try {
            CachedWebSocketSessionObject cachedObj = xTermCachedObjMap.get(webSocketSession.getId());
            if (null == cachedObj) {
                if (currentSessionCnt.get() >= maxSessionCnt) {
                    log.error("Execute max concurrent capacity exec.max:{},current:{}", maxSessionCnt, currentSessionCnt.get());
                    webSocketSession.sendMessage(new TextMessage("Execute Max Concurrent Capacity,session will be closed."));
                    webSocketSession.close();
                    return false;
                }
                Session session = createSessionFromSshInfo(sshInfo);
                cachedObj = new CachedWebSocketSessionObject();
                cachedObj.setSshInfo(sshInfo);
                cachedObj.setSshSession(session);

                SshInfo sshInfoObject = composeSshInfo(sshInfo);
                String properJavaRDPFull = webSshConfig.getProperJavaRdpJarFilePath() + File.separator + webSshConfig.getProperJavaRdpJar();

                if (sshInfoObject.getRdpConfig() != null && sshInfoObject.getRdpConfig().shouldEnsureProperJavaRDPJarExist()) {
                    //ensure javaRDP jar file
                    String token = (String) webSocketSession.getAttributes().get(webSshConfig.getTokenName());
                    ensureChannelSftpAndExec(sshInfo, token, sftp -> {
                        boolean fileExist = false;

                        try {
                            SftpATTRS attr = sftp.stat(properJavaRDPFull);
                            long fileSize = attr.getSize();
                            if (fileSize > 0) {
                                fileExist = true;
                            }
                        } catch (Exception e) {
                            log.error("error will find rdp java jar. or file not exist.", e);
                        }
                        if (!fileExist) {
                            InputStream javaRdpJar = webSshJavaRdpService.getProperJavaRdpJarStream();
                            try {
                                if (null == javaRdpJar || javaRdpJar.available() <= 0) {
                                    log.error("can't get java rdp jar file...");
                                } else {
                                    //make dir and copy java rdp jar
                                    sftp.mkdir(webSshConfig.getProperJavaRdpJarFilePath());
                                    sftp.put(javaRdpJar, properJavaRDPFull);
                                }
                            } catch (Exception e) {
                                log.error("error try uplaod java rdp jar.", e);
                            }
                        }
                    });
                }

                cachedObj.setClientIp(webSocketSession.getRemoteAddress().getAddress().getHostAddress());
                // seems need to set... try set model....
                CachedWebSocketSessionObject finalCachedObj = cachedObj;
                Channel channel = createXtermShellChannel(session, (inputStream, outputStream) -> {
                    finalCachedObj.setChannelInputStream(inputStream);
                    finalCachedObj.setChannelOutputStream(outputStream);
                    //if rdp request.
                    //auto connect rdp here
                    if (sshInfoObject.getRdpConfig() != null && sshInfoObject.getRdpConfig().shouldSendAutoConnectRdpCmd()) {
                        String rdpRunCmd = String.format("%s -jar %s %s", webSshConfig.getJavaFullPath(), properJavaRDPFull, sshInfoObject.getRdpConfig().buildArgsStr());
                        log.info("run rdo command:{}", rdpRunCmd);
                        try {
                            outputStream.write(rdpRunCmd.getBytes(StandardCharsets.UTF_8));
                            outputStream.flush();
                        } catch (Exception e) {
                            log.error("write rpd run command to xterm error,cmd:" + rdpRunCmd, e);
                        }
                    }
                }, webSshConfig.getDefaultConnectTimeOut(), xTermShellSize(webSocketSession), composeSshInfo(sshInfo));
                cachedObj.setSshChannel(channel);

                //try start rdp thread and cache it.
                /*cachedObj.setRdpThread(new Thread(()->{
                    SshInfo sshInfoObject = null;
                    try {
                        sshInfoObject = composeSshInfo(sshInfo);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if (null != sshInfoObject && null != sshInfoObject.getRdpConfig() && sshInfoObject.getRdpConfig().isRdp()) {
                        //TODO copy properJavaRDP jar to path.
                        if(sshInfoObject.getRdpConfig().isDirectConnectRdpServer()){
                            //exec properJava?
                            //create one thread and run properJava and cacheIt.
                            //while close xterm close thread.
                            //start rdp.
                            try {
                                Rdesktop.main(sshInfoObject.getRdpConfig().buildArgs());
                            } catch (RdesktopException e) {
                                log.error("error while execute rdp client.", e);
                                sendMessage2Websocket(webSocketSession,String.format("error while execute rdp client.error:%s",e.getMessage()));
                            }
                        }
                    }
                }));*/

                xTermCachedObjMap.put(webSocketSession.getId(), cachedObj);
                sendSshMessageBack(webSocketSession, channel);
            }
            return true;
        } catch (JsonProcessingException e) {
            log.error("error parse sshInfo object。sshInfo:{},exception:{}", sshInfo, e);
            webSocketSession.sendMessage(new TextMessage(e.getMessage()));
            webSocketSession.close();
        } catch (JSchException e) {
            log.error("error login to jsch.sshInfo:{},exception:{}", sshInfo, e);
            webSocketSession.sendMessage(new TextMessage(e.getMessage()));
            webSocketSession.close();
        } catch (Exception e) {
            log.error("error when init ssh connection to server.sshInfo:{},exception:{}", sshInfo, e);
            webSocketSession.sendMessage(new TextMessage(e.getMessage()));
            webSocketSession.close();
        }
        return false;
    }

    @Override
    public boolean handleTermWebSshMsg(WebSocketSession webSocketSession, TextMessage message) throws IOException {
        //send message back
        //webSocketSession.sendMessage(message);
        //https://blog.csdn.net/xincang_/article/details/129054940
        //https://www.jianshu.com/p/db8a860b286c
        //then send cmd to ssh term
        OutputStream outputStream = xTermCachedObjMap.get(webSocketSession.getId()).getChannelOutputStream();
        //write cmd to jsch
        outputStream.write(message.getPayload().getBytes());
        outputStream.flush();
        return true;
    }

    int wpColRate() {
        return 8;
    }

    int hpRowRate() {
        return 20;
    }

    @Override
    public boolean handleTermWebSShResize(WebSocketSession webSocketSession, TextMessage message, int rows, int cols) {
        int row = rows > 0 ? rows : webSshConfig.getWebSshTermRow();
        int col = cols > 0 ? cols : webSshConfig.getWebSshTermCol();
        int wp = col * wpColRate();
        int hp = row * hpRowRate();
        log.info("handler resize,row:{},col;{},wp:{},hp:{}", row, col, wp, hp);
        CachedWebSocketSessionObject cachedObj = xTermCachedObjMap.get(webSocketSession.getId());
        //then send cmd to ssh term
        Channel channel = cachedObj.getSshChannel();
        ((ChannelShell) channel).setPtySize(col, row, wp, hp);
        log.info("handler resize success!");
        return true;
    }

    /**
     * @param webSocketSession
     * @return
     */
    public CachedWebSocketSessionObject getCachedWebSocketSessionObject(WebSocketSession webSocketSession) {
        return xTermCachedObjMap.get(webSocketSession.getId());
    }

    @Override
    public boolean onSessionClose(WebSocketSession webSocketSession) {
        if (xTermCachedObjMap.containsKey(webSocketSession.getId())) {
            CachedWebSocketSessionObject cachedWebSocketSessionObject = xTermCachedObjMap.get(webSocketSession.getId());
            String token = (String) webSocketSession.getAttributes().get(webSshConfig.getTokenName());
            //ensure session create for file upload is closed.
            ensureSessionClose(cachedWebSocketSessionObject.getSshInfo(), token, cachedWebSocketSessionObject.getClientIp());

            //stop rdp thread while close session.
            Thread rdpThread = cachedWebSocketSessionObject.getRdpThread();
            if (null != rdpThread && rdpThread.isAlive()) {
                rdpThread.stop();
            }

            xTermCachedObjMap.get(webSocketSession.getId()).close();
            xTermCachedObjMap.remove(webSocketSession.getId());
        }

        return true;
    }
}
