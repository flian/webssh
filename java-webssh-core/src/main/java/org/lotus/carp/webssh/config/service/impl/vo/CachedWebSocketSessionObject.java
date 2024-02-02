package org.lotus.carp.webssh.config.service.impl.vo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.vo.SshInfo;

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
    private SshInfo sshInfo;

    /**
     * ssh sshChannel
     */
    private Channel sshChannel;

    /**
     * ssh session
     */
    private Session sshSession;

    /**
     * command current user typed.
     */
    private StringBuffer command = new StringBuffer();

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
