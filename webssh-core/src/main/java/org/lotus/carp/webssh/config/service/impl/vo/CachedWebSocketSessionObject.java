package org.lotus.carp.webssh.config.service.impl.vo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.vo.SshInfo;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-02 16:12
 **/
@Data
@Slf4j
public class CachedWebSocketSessionObject {
    /**
     * ssh connection info
     */
    private String sshInfo;

    /**
     * ssh sshChannel
     */
    private Channel sshChannel;


    /**
     * ssh session
     */
    private Session sshSession;

    /**
     * channel InputStream cache
     */
    private InputStream channelInputStream;

    /**
     * channel OutputStream cache
     */
    private OutputStream channelOutputStream;

    /**
     * client ip addr
     */
    private String clientIp;

    public boolean close() {

        if (null != sshChannel) {
            sshChannel.disconnect();
        }

        if (null != sshSession) {
            sshSession.disconnect();
        }
        return true;
    }
}
