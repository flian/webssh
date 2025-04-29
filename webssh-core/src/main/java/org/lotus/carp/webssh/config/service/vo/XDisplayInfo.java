package org.lotus.carp.webssh.config.service.vo;

import lombok.Data;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-04-30 16:31
 **/
@Data
public class XDisplayInfo {
    private String x11Host;
    private int x11Port;

    public static XDisplayInfo composeFromString(String str) {
        XDisplayInfo result = new XDisplayInfo();
        String[] temp = str.split(":");
        result.x11Host = temp[0];
        result.x11Port = Integer.parseInt(temp[1]);
        return result;
    }
}
