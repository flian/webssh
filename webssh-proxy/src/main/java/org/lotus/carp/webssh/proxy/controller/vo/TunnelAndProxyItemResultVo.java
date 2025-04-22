package org.lotus.carp.webssh.proxy.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : foy
 * @date : 2025/4/22:10:07
 **/
@Data
public class TunnelAndProxyItemResultVo {

    /**
     * proxy type 0 socket proxy,1 http proxy
     */
    String proxyType;
    //http or https
    String schema;
    //host info here
    String host;
    //server port info
    int port;
    List<TunnelAndProxyInfoItemResultVo> items;
}
