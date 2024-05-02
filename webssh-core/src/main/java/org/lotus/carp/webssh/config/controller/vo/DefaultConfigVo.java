package org.lotus.carp.webssh.config.controller.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p>default config for client.</p>
 *
 * @author : foy
 * @date : 2024-02-01 11:37
 **/
@Data
public class DefaultConfigVo {
    /**
     * client should save password?
     */
    private boolean savePass;

    /**
     * client should verify token,if not token present,will show login form.
     */
    private boolean shouldVerifyToken;

    /**
     * is shell for windows rdp connection
     * if this is true, it means use ssh to linux server then x11 forwarding to windows server
     */
    private boolean rdp = false;

    /**
     * is direct connect  to rdp windows server. default is false.
     * set is true if you wish conenct to rdp server when connnect is create.
     */
    private boolean directConnectRdpServer = false;

    /**
     * x11 forwarding
     */
    private String x11Display = "localhost:0";

    /**
     * rdp server ip
     */
    private String windowsIp = "";

    /**
     * windows rdp port. -t NUM
     */
    private int rdpPort = 3389;

    /**
     *full-screen mode [with Linux KDE optimization]
     */
    private boolean rdpWindowsFullScreen = false;

    /**
     * rdp windows size. -g 1024x768
     */
    private String rpdWindowsSize = "1024x768";

    /**
     * rdp log level . default is : -l INFO
     */
    private String logLevel = "INFO";
}
