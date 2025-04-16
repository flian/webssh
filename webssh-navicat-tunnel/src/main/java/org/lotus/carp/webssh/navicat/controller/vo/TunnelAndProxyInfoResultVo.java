package org.lotus.carp.webssh.navicat.controller.vo;

import lombok.Data;

/**
 * @author : foy
 * @date : 2025/4/16:15:39
 **/
@Data
public class TunnelAndProxyInfoResultVo {
    public static final String LINK_TYPE = "link";
    public static final String DEFAULT_TYPE = "default";
    public static final String SOCKET_PROXY_TYPE = "socketProxy";
    private String type = DEFAULT_TYPE;
    private String url = "";
    private boolean tunnel = true;
    private String meme;
    private String host = "";
    private String port = "";
}
