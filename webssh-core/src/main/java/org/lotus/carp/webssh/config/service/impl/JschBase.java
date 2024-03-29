package org.lotus.carp.webssh.config.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.config.service.vo.SshInfo;
import org.lotus.carp.webssh.config.utils.WebSshUtils;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.config.websocket.websshenum.WebSshLoginTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import static org.lotus.carp.webssh.config.websocket.websshenum.WebSshLoginTypeEnum.PASSWORD_LOGIN_TYPE;

/**
 * <h3>javaWebSSH</h3>
 * <p>jsch base service method</p>
 *
 * @author : foy
 * @date : 2024-02-21 17:06
 **/
@Slf4j
public class JschBase implements InitializingBean {
    @Resource
    protected WebSshConfig webSshConfig;

    private static boolean jschLoggerInitialized = false;

    private ObjectMapper baseObjectMapper = new ObjectMapper();

    Cache<String, Session> sessionCache;

    /**
     * create jsch sftp
     *
     * @param sshInfo
     * @param token
     * @return
     * @throws IOException
     * @throws JSchException
     */
    void ensureChannelSftpAndExec(String sshInfo, String token, EnsureCloseSftpCall sftpCallFunc) {
        if (ObjectUtils.isEmpty(sshInfo) || null == sftpCallFunc) {
            throw new WebSshBusinessException("sshInfo and sftpCallFunc should not be null.");
        }
        ensureChannelSftpAndExec(sshInfo, token, webSshConfig.getDefaultConnectTimeOut(), sftpCallFunc);
    }

    private Session ensureGetOrCreateSession(String sshInfo, String token) throws IOException, JSchException {

        String preFix = token;
        if (ObjectUtils.isEmpty(preFix)) {
            preFix = clientRemoteHost();
        }

        String cacheSessionKey = md5String(String.format("%s:%s", preFix, sshInfo));
        Session result = sessionCache.getIfPresent(cacheSessionKey);
        if (null != result) {
            return result;
        }
        return createAndCacheOne(sshInfo, cacheSessionKey);
    }

    private String md5String(String source) {
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }

    private synchronized Session createAndCacheOne(String sshInfo, String cacheKey) throws IOException, JSchException {
        Session session = createSessionFromSshInfo(sshInfo);
        if (null != session) {
            sessionCache.put(cacheKey, session);
        }
        return session;
    }

    private String clientRemoteHost() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request.getRemoteHost();
    }

    /**
     * create jsch sftp
     *
     * @param sshInfo
     * @param token
     * @param connectTimeout
     * @return
     * @throws IOException
     * @throws JSchException
     */
    void ensureChannelSftpAndExec(String sshInfo, String token, int connectTimeout, EnsureCloseSftpCall sftpCallFunc) {
        if (ObjectUtils.isEmpty(sshInfo) || null == sftpCallFunc) {
            throw new WebSshBusinessException("sshInfo and sftpCallFunc should not be null.");
        }
        ChannelSftp sftp = null;
        try {
            Session session = ensureGetOrCreateSession(sshInfo, token);
            Channel channel = session.openChannel("sftp");
            channel.connect(connectTimeout);
            sftp = (ChannelSftp) channel;
            sftpCallFunc.callSftp(sftp);
        } catch (Exception e) {
            log.error("error while process sftp op.", e);
            throw new WebSshBusinessException("500,server error." + e.getMessage());
        } finally {
            //close sftp and session.
            if (null != sftp)
                try {
                    sftp.exit();
                } catch (Exception e) {
                    log.error("error close sftp.", e);
                }
            //session maybe cached,don't close it.
            /*if (null != session) {
                try {
                    session.disconnect();
                } catch (Exception e) {
                    log.error("error clsoe jsch session.", e);
                }
            }*/
        }
    }

    /**
     * create jsch session
     *
     * @param sshInfo
     * @return
     * @throws IOException
     * @throws JSchException
     */
    Session createSessionFromSshInfo(String sshInfo) throws IOException, JSchException {
        return createSessionFromSshInfo(sshInfo, webSshConfig.getDefaultConnectTimeOut());
    }

    /**
     * create jsch session
     *
     * @param sshInfo
     * @param connectTimeout
     * @return
     * @throws IOException
     * @throws JSchException
     */
    Session createSessionFromSshInfo(String sshInfo, int connectTimeout) throws IOException, JSchException {
        SshInfo sshInfoObject = composeSshInfo(sshInfo);
        JSch jsch = new JSch();
        Hashtable<String, String> config = new Hashtable();
        config.put("StrictHostKeyChecking", "no");
        WebSshLoginTypeEnum loginType = WebSshLoginTypeEnum.getByCode(sshInfoObject.getLogintype());

        switch (loginType) {
            case PASSWORD_LOGIN_TYPE: {
                config.put("PreferredAuthentications", "password");
                if (webSshConfig.isSshPrivateKeyOnly()) {
                    throw new WebSshBusinessException("only allow private key to login to remote server,but try password login.");
                }
                break;
            }
            case PRIVATE_KEY_LOGIN_TYPE: {
                config.put("PreferredAuthentications", "publickey");
                //add private key
                jsch.addIdentity("jsch_xterm_private_key", sshInfoObject.getPassword().getBytes(), (byte[]) null, (byte[]) null);
                //jsch.addIdentity(sshInfoObject.getPassword());
                break;
            }
        }
        jsch.setConfig(config);
        Session session = jsch.getSession(sshInfoObject.getUsername(), sshInfoObject.getIpaddress(), sshInfoObject.getPort());

        if (PASSWORD_LOGIN_TYPE == loginType) {
            session.setPassword(sshInfoObject.getPassword());
        }

        session.connect(connectTimeout);

        return session;
    }

    private SshInfo composeSshInfo(String sshInfo) throws JsonProcessingException {
        return baseObjectMapper.readValue(deCodeBase64Str(sshInfo), SshInfo.class);
    }

    /**
     * create jsch xterm channel shell for webssh with default parameters
     *
     * @param session     session to use create channel
     * @param channelCall get channel input and output stream after channel create.
     * @param ptySize     ptySize[0]  col – terminal width;
     *                    ptySize[1]  columns row – terminal height
     *                    ptySize[2]  rows wp – terminal width, pixels
     *                    ptySize[3]  hp – terminal height, pixels
     * @return
     * @throws JSchException
     * @@param channelCall get channel input and output stream after channel create.
     */
    Channel createXtermShellChannel(Session session, CreateXtermShellChannelCall channelCall, int[] ptySize) throws JSchException {
        return createXtermShellChannel(session, channelCall, webSshConfig.getDefaultConnectTimeOut(), ptySize);
    }

    /**
     * create jsch xterm channel shell for webssh
     *
     * @param session        session to use create channel
     * @param channelCall    get channel input and output stream after channel create.
     * @param connectTimeout channel connect timeout.
     * @param ptySize        ptySize[0]  col – terminal width;
     *                       ptySize[1]  columns row – terminal height
     *                       ptySize[2]  rows wp – terminal width, pixels
     *                       ptySize[3]  hp – terminal height, pixels
     * @return
     * @throws JSchException
     */
    Channel createXtermShellChannel(Session session, CreateXtermShellChannelCall channelCall, int connectTimeout, int[] ptySize) throws JSchException {
        Channel channel = session.openChannel("shell");
        ((ChannelShell) channel).setPtyType(getTermPtyType());
        ((ChannelShell) channel).setPtySize(ptySize[0], ptySize[1], ptySize[2], ptySize[3]);
        ((ChannelShell) channel).setPty(true);
        // should set mode
        ((ChannelShell) channel).setTerminalMode(composeTerminalModes());

        try {
            InputStream inputStream = channel.getInputStream();
            channel.connect(connectTimeout);
            OutputStream outputStream = channel.getOutputStream();
            if (null != channelCall) {
                channelCall.applySetInputAndOutStream(inputStream, outputStream);
            }
        } catch (IOException e) {
            log.error("error create xterm shell/", e);
            return null;
        }

        return channel;
    }

    String getTermPtyType() {
        if (!ObjectUtils.isEmpty(webSshConfig.getWebSshTermPtyType())) {
            return webSshConfig.getWebSshTermPtyType();
        }
        return "vt100";
    }

    byte translateBoolean2Byte(boolean flag) {
        if (flag) {
            return (byte) 1;
        }
        return 0;
    }

    String deCodeBase64Str(String sshInfo) {
        return new String(Base64.getDecoder().decode(sshInfo));
    }

    //@see https://stackoverflow.com/questions/24623170/an-example-of-how-to-specify-terminal-modes-pty-req-string-for-ssh-client?rq=1
    byte[] composeTerminalModes() {
        //can see "tail -f /var/log/secure" in your linux server for more login error detail.
        byte[] terminalModes = {
                //Translate uppercase characters to lowercase.
                37,
                0, 0, 0, translateBoolean2Byte(webSshConfig.isWebSshTermTranslate2Lowercase()),
                //ECHO 53
                53,
                0, 0, 0, translateBoolean2Byte(webSshConfig.isWebSshTermEcho()),
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
                // TTY_OP_ISPEED 128=0x80
                (byte) 0x80,
                // 38400 = 0x9600
                0, 0, (byte) 0x96, (byte) 0x00,
                // TTY_OP_OSPEED 129=0x81
                (byte) 0x81,
                // 38400 again
                0, 0, (byte) 0x96, (byte) 0x00,
                // TTY_OP_END
                0,
        };
        return terminalModes;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!jschLoggerInitialized && webSshConfig.getDebugJsch2SystemError()) {
            JSch.setLogger(new JschLogger());
        }
        if (!WebSshUtils.isInitialized) {
            if (!ObjectUtils.isEmpty(webSshConfig.getDateFormat())) {
                WebSshUtils.WEB_SSH_DEFAULT_DATE_FORMAT = webSshConfig.getDateFormat();
            }
            WebSshUtils.isInitialized = true;
        }

        sessionCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterAccess(webSshConfig.getTokenExpiration(), TimeUnit.HOURS)
                .removalListener(rmItem -> {
                    //close session to ensure jsch close normally.
                    log.info("remove session key cache,key:{},reason:{}", rmItem.getKey(), rmItem.getCause());
                    if (rmItem.getValue() instanceof Session) {
                        ((Session) rmItem.getValue()).disconnect();
                    }
                })
                .build();
        //init sub class.
        subInit();
    }

    /**
     * afterPropertiesSet init for sub class.
     */
    public void subInit() {

    }
}
