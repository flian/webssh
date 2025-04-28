package org.lotus.carp.webssh.config.websocket.expoint;

import java.util.Map;

/**
 * @author : foy
 * @date : 2025/4/28:15:34
 **/
public interface WebSshWebsocketHandshakeInterceptorHook {

    default void setIfNotNull(String key, String val, Map<String, Object> attributes) {
        if (null != key && null != val) {
            attributes.put(key, val);
        }
    }

    /**
     * should WebSshWebSocketHandshakeInterceptor go ahead or just return true.
     * @param currentRequestWsUri current websocket url
     * @return true:should process connect. false: not care it.
     */
    boolean shouldApplyBeforeHandshake(String currentRequestWsUri);

    /**
     * sub class should apply
     * @param requestUri
     * @param paramMap
     * @param attributes
     */
    void setSessionParamIfPresent(String requestUri, Map<String, String> paramMap, Map<String, Object> attributes);
}
