package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 11:14
 **/
@Data
public class PropertiesConfigUser {
    /**
     * username
     */
    private String username;
    /**
     * password
     */
    private String password;
    /**
     * allowed ips for given user. % means allowed any ip connection.
     */
    private String allowedIps = "%";
}
