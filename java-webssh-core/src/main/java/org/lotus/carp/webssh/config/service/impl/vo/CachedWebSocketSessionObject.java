package org.lotus.carp.webssh.config.service.impl.vo;

import com.jcraft.jsch.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.service.vo.SshInfo;

import java.io.IOException;
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
    private SshInfo sshInfo;
    /**
     * ssh term input stream
     */
    private InputStream in;

    /**
     * ssh term output stream
     */
    private OutputStream out;

    /**
     * ssh sshChannel
     */
    private Channel sshChannel;

    public boolean close() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                log.error("error while close jsch term input stream. ", e);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                log.error("error while close jsch term output stream. ", e);
            }
        }
        if (null != sshChannel) {
            sshChannel.disconnect();
        }
        return true;
    }
}
