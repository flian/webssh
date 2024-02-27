package org.lotus.carp.webssh.config.service;

import org.lotus.carp.webssh.config.service.vo.PropertiesConfigUser;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginResultVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginVo;

import java.util.UUID;

/**
 * <h3>javaWebSSH</h3>
 * <p>prepare user login</p>
 *
 * @author : foy
 * @date : 2024-01-31 11:05
 **/
public interface WebSshLoginService {
    /**
     * try to websocket login
     * @param webSshLoginVo
     * @return
     */
    WebSshLoginResultVo doWebSshLogin(WebSshLoginVo webSshLoginVo);

    /**
     * verify token is valid
     * @param token
     * @return
     */
    Boolean isTokenValid(String token);

    /**
     * gen token for config user
     * @param configUser
     * @return
     */
    default String genTokenForUser(PropertiesConfigUser configUser){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // delete '-' char
        str = str.replace("-", "");
        return str;
    }
}
