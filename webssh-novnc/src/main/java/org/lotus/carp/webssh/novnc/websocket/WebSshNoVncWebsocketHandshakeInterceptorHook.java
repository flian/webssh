package org.lotus.carp.webssh.novnc.websocket;

import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.lotus.carp.webssh.config.websocket.expoint.WebSshWebsocketHandshakeInterceptorHook;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : foy
 * @date : 2025/4/28:15:49
 **/
public class WebSshNoVncWebsocketHandshakeInterceptorHook implements WebSshWebsocketHandshakeInterceptorHook {

    @Resource
    private WebSshConfig webSshConfig;

    public static final String NO_VNC_TARGET_HOST = "noVncTargetHost";
    public static final String NO_VNC_TARGET_PORT = "noVncTargetPort";

    public static final String NO_VNC_TARGET_IS_LINUX_OS = "isLinuxOs";

    @Override
    public boolean shouldApplyBeforeHandshake(String currentRequestWsUri) {
        return !ObjectUtils.isEmpty(currentRequestWsUri) && currentRequestWsUri.contains(webSshConfig.getWebSshNoVncWebsocketPrefix());
    }

    @Override
    public void setSessionParamIfPresent(String requestUri, Map<String, String> paramMap, Map<String, Object> attributes) {
        setIfNotNull(NO_VNC_TARGET_HOST, paramMap.get(NO_VNC_TARGET_HOST), attributes);
        setIfNotNull(NO_VNC_TARGET_PORT, paramMap.get(NO_VNC_TARGET_PORT), attributes);
        setIfNotNull(NO_VNC_TARGET_IS_LINUX_OS, paramMap.get(NO_VNC_TARGET_IS_LINUX_OS), attributes);
    }
}
