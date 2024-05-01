package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-02 15:46
 **/
@Data
public class SshInfo {
    /**
     * linux server ip
     */
    private String ipaddress;
    /**
     * linux server ssh port,default 22
     */
    private int port = 22;
    /**
     * linux server username
     */
    private String username;
    /**
     * linux server password
     */
    private String password;
    /**
     * login type. 0:password,1:auth key
     */
    private String logintype;


    /**
     * is shell for windows rdp connection
     * if this is true, it means use ssh to linux server then x11 forwarding to windows server
     */
    private boolean rdp = true;

    /**
     * is user manual collect to rdp windows server. default is true.
     * set is false if you wish conenct to rdp server when connnect is create.
     */
    private boolean manualConnectRdp = true;

    /**
     * x11 forwarding
     */
    private String xDisplay = "localhost:0";

    /**
     * rdp server ip
     */
    private String windowsIp;

    /**
     * rdp windows title. -t
     */
    private String title = "java-web-ssh properJavaRDP";

    /**
     * windows rdp port. -t NUM
     */
    private int rdpPort = 3389;

    /**
     * windows rdp user name. -u
     */
    private String rdpUser;

    /**
     * windows rdp password. -p
     */
    private String rdpPassword;

    /**
     * rdp windows size. -g 1024x768
     */
    private String rpdWindowsSize = "1024x768";

    /**
     * rdp log level . default is : -l INFO
     */
    private String logLevel = "INFO";

    /**
     * rdp disk device map,example: --disk_device_map share@D:\\Download,test@D:\\upload
     */
    private String rdpDiskDeviceMap;

}
