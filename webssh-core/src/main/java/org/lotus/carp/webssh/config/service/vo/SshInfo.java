package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-02 15:46
 **/
@Data
@Slf4j
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
    private boolean rdp = false;

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
    private String windowsIp = "192.168.2.16";

    /**
     * rdp windows title. -T
     */
    private String title = "java-web-ssh properJavaRDP - %s";

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

    /**
     * rdp disk device map,example: --disk_device_map share@D:\\Download,test@D:\\upload
     */
    private String rdpDiskDeviceMap;

    public RdpValidResult isRdpArgumentsValid() {
        if (StringUtils.isEmpty(windowsIp)) {
            log.warn("rdp ip is empty.");
            return RdpValidResult.RdpValidResultError(String.format("rdp ip is empty. ip=%s",windowsIp));
        }
        if (rdpPort <= 0) {
            log.warn("rdp port is invalid.");
            return RdpValidResult.RdpValidResultError(String.format("rdp port is invalid. port=%s",rdpPort));
        }
        return RdpValidResult.OK;
    }

    public String[] buildArgs() {
        List<String> args = new ArrayList<>();
        if (!StringUtils.isEmpty(title)) {
            args.add("-T");
            args.add(String.format(title,windowsIp));
        }
        if(rdpPort > 0){
            args.add("-t");
            args.add(""+rdpPort);
        }

        if(!StringUtils.isEmpty(rdpUser)){
            args.add("-u");
            args.add(rdpUser);
        }

        if(!StringUtils.isEmpty(rdpPassword)){
            args.add("-p");
            args.add(rdpPassword);
        }

        if(rdpWindowsFullScreen){
            args.add("-fl");
        }else if(!StringUtils.isEmpty(rpdWindowsSize)){
            args.add("-g");
            args.add(rpdWindowsSize);
        }

        if (!StringUtils.isEmpty(logLevel)) {
            args.add("-l");
            args.add(logLevel);
        }

        if (!StringUtils.isEmpty(rdpDiskDeviceMap)) {
            args.add("--disk_device_map");
            args.add(rdpDiskDeviceMap);
        }

        args.add(windowsIp);
        String[] result = new String[args.size()];
        args.toArray(result);
        return result;
    }
}
