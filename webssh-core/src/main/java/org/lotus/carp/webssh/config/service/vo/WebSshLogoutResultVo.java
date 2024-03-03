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
public class WebSshLogoutResultVo {

    private boolean logoutResult;
    /**
     * token username
     */
    private String userName;

    /**
     * login user ip address
     */
    private String loginUserIpAddr;

    /**
     * logout request ip.
     */
    private String logoutFromIpAddr;


}
