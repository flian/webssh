package org.lotus.carp.webssh.config.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.BusinessException;
import org.lotus.carp.webssh.config.service.vo.SshInfo;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Hashtable;

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
    private WebSshConfig webSshConfig;

    private static boolean jschLoggerInitialized = false;

    private ObjectMapper baseObjectMapper = new ObjectMapper();

    /**
     * create jsch sftp
     *
     * @param sshInfo
     * @return
     * @throws IOException
     * @throws JSchException
     */
    void ensureCreateChannelSftpAndExec(String sshInfo, EnsureCloseSftpCall sftpCallFunc) {
        if (ObjectUtils.isEmpty(sshInfo) || null == sftpCallFunc) {
            throw new BusinessException("sshInfo and sftpCallFunc should not be null.");
        }
        ensureCreateChannelSftpAndExec(sshInfo, webSshConfig.getDefaultConnectTimeOut(), sftpCallFunc);
    }

    /**
     * create jsch sftp
     *
     * @param sshInfo
     * @param connectTimeout
     * @return
     * @throws IOException
     * @throws JSchException
     */
    void ensureCreateChannelSftpAndExec(String sshInfo, int connectTimeout, EnsureCloseSftpCall sftpCallFunc) {
        if (ObjectUtils.isEmpty(sshInfo) || null == sftpCallFunc) {
            throw new BusinessException("sshInfo and sftpCallFunc should not be null.");
        }
        Session session = null;
        ChannelSftp sftp = null;
        try {
            session = createSessionFromSshInfo(sshInfo);
            Channel channel = session.openChannel("sftp");
            channel.connect(connectTimeout);
            sftp = (ChannelSftp) channel;
            sftpCallFunc.callSftp(sftp);
        } catch (Exception e) {
            log.error("error while process sftp op.", e);
        } finally {
            //close sftp and session.
            if (null != sftp)
                sftp.exit();
            if (null != session) {
                session.disconnect();
            }
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
        SshInfo sshInfoObject = baseObjectMapper.readValue(deCodeBase64Str(sshInfo), SshInfo.class);
        JSch jsch = new JSch();
        Hashtable<String, String> config = new Hashtable();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        jsch.setConfig(config);
        Session session = jsch.getSession(sshInfoObject.getUsername(), sshInfoObject.getIpaddress(), sshInfoObject.getPort());

        session.setPassword(sshInfoObject.getPassword());

        session.connect(connectTimeout);

        return session;
    }

    /**
     * create jsch xterm channel shell for webssh with default parameters
     *
     * @param session
     * @return
     * @throws JSchException
     */
    Channel createXtermShellChannel(Session session) throws JSchException {
        return createXtermShellChannel(session, webSshConfig.getDefaultConnectTimeOut(), 80, 24, 640, 480);
    }

    /**
     * create jsch xterm channel shell for webssh
     *
     * @param session
     * @param connectTimeout
     * @param col            shell col
     * @param row            shell row
     * @param wp             shell col for px
     * @param hp             shell row for px
     * @return
     * @throws JSchException
     */
    Channel createXtermShellChannel(Session session, int connectTimeout, int col, int row, int wp, int hp) throws JSchException {
        Channel channel = session.openChannel("shell");
        ((ChannelShell) channel).setPtyType("xterm");
        ((ChannelShell) channel).setPtySize(col, row, wp, hp);
        ((ChannelShell) channel).setPty(true);
        // should set mode
        ((ChannelShell) channel).setTerminalMode(composeTerminalModes());
        channel.connect(connectTimeout);
        return channel;
    }

    String deCodeBase64Str(String sshInfo) {
        return Base64Decoder.decodeStr(sshInfo);
    }

    //@see https://stackoverflow.com/questions/24623170/an-example-of-how-to-specify-terminal-modes-pty-req-string-for-ssh-client?rq=1
    byte[] composeTerminalModes() {
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
    }
}
