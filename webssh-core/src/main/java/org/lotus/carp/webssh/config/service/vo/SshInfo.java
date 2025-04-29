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
     * rdp config for windows rdp
     */
    private RdpConfig rdpConfig;
}
