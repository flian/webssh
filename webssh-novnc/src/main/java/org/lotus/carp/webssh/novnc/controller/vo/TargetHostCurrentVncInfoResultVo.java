package org.lotus.carp.webssh.novnc.controller.vo;

import lombok.Data;

/**
 * @author : foy
 * @date : 2025/4/27:15:12
 **/
@Data
public class TargetHostCurrentVncInfoResultVo {
    private String host;
    private boolean isLinux;
    private int nextPort;
    private int currentVncClientCnt;
    private String vncHtmlUrl;
}
