package org.lotus.carp.webssh.proxy.config;

/**
 * @author : foy
 * @date : 2025/4/22:10:14
 **/
public class WebSshProxyConstants {
    /**
     * proxy type socket
     */
    public static final int SOCKET_PROXY_TYPE = 0;
    /**
     * proxy type http
     */
    public static final int HTTP_PROXY_TYPE = 1;
    /**
     * restart proxy
     */

    public static final int OP_RESTART = -1;
    /**
     * start proxy
     */
    public static final int OP_START = 0;
    /**
     * stop proxy
     */
    public static final int OP_STOP = 1;
}
