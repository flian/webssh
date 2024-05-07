package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class RdpConfig {
    /**
     * is shell for windows rdp connection
     * if this is true, it means use ssh to linux server then x11 forwarding to windows server
     */
    private boolean rdp = false;

    /**
     * enable this to config rdp server info
     */
    private boolean rdpServer = false;

    /**
     * if rdp = true and rdpServer=true, if this is true, it will direct connect rdp server by config.
     */
    private boolean autoConnect = false;

    /**
     * x11 forwarding. install MobaXterm frist.
     */
    private String x11Display = "localhost:0";

    /**
     * rdp server ip
     */
    private String windowsIp = "";

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
     * full-screen mode [with Linux KDE optimization]
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

    public boolean shouldEnableXForwarding(){
        //FIXME XForwarding should be next topic.
        if(rdp && rdpServer && autoConnect){
            return true;
        }
        return false;
    }

    /**
     * should ensure properJavaRDP jar in temp folder
     * @return
     */
    public boolean shouldEnsureProperJavaRDPJarExist(){
        if(rdp && rdpServer && autoConnect){
            return true;
        }
        return false;
    }

    /**
     * should auto connect rdp server
     * @return
     */
    public boolean shouldSendAutoConnectRdpCmd(){
        if(rdp && rdpServer && autoConnect){
            return true;
        }
        return false;
    }

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
    public List<String> buildArgsList(){
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

        if(rdpWindowsFullScreen || (!StringUtils.isEmpty(rpdWindowsSize) && !rpdWindowsSize.contains("x"))){
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
        return args;
    }

    public String[] buildArgs() {
        List<String> args = buildArgsList();
        String[] result = new String[args.size()];
        args.toArray(result);
        return result;
    }

    public String buildArgsStr(){
        List<String> args = buildArgsList();
        return args.stream().collect(Collectors.joining(" "));
    }
}
