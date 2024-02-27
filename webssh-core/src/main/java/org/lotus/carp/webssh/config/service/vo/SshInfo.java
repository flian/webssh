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
    private String ipaddress;
    private int port = 22;
    private String username;
    private String password;
    private String logintype;
}
