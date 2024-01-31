package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 11:07
 **/
@Data
public class WebSshLoginResultVo {
    /**
     * token username
     */
    private String userName;

    /**
     * login user ip address
     */
    private String userIpAddr;

    /**
     * token string
     */
    private String token;
    /**
     * token will expired at, default format yyyy-MM-dd HH:mm:ss
     */
    private String expired;

    /**
     * expiration time the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this date.
     */
    private long expirationTms;
}
