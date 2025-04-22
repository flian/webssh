package org.lotus.carp.webssh.proxy.controller.vo;

import lombok.Data;

/**
 * @author : foy
 * @date : 2025/4/16:15:39
 **/
@Data
public class TunnelAndProxyInfoItemResultVo {
    private int proxyType;
    private String meme;
    private String host = "";
    private String port = "";
    private String username = "";
    private String password = "";
    private String httpProxyUrl = "";
    private boolean isRunning = false;
}
