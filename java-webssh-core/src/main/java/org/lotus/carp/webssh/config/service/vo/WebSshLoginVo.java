package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p>login vo</p>
 *
 * @author : foy
 * @date : 2024-01-31 11:06
 **/
@Data
public class WebSshLoginVo {
    private String username;
    private String password;
    private String requestIp;
}
