package org.lotus.carp.webssh.proxy.controller.vo;

import lombok.Data;

/**
 * @author : foy
 * @date : 2025/4/21:15:30
 **/
@Data
public class ProxyOpRequestVo {
    /**
     * proxy type 0 socket proxy,1 http proxy
     */
    private int proxyType;

    /**
     * proxy operator,
     * -1-try to restart proxy
     * 0-start proxy,
     * 1-stop proxy
     */
    private int op;

    /**
     * bind ip address,default should be 0.0.0.0,means all ip is ok
     */
    private String bindIp;
    /**
     * proxy bind port
     */
    private String bindPort;

    /**
     * enable or disable no auth
     * 0: mush use username/password auth
     * 1: no need auth
     */
    private int enableNoAuth = 0;

    /**
     * auth username
     */
    private String username;

    /**
     * auth password
     */
    private String password;
}
