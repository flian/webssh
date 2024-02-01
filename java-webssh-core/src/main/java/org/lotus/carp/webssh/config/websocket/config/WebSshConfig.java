package org.lotus.carp.webssh.config.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 14:49
 **/
@Data
@Component
@ConfigurationProperties(prefix = "webssh")
public class WebSshConfig {

    /**
     * should front and back end verify token before process
     */
    private boolean shouldVerifyToken = true;


    /**
     * config user delimiter
     */
    private String userDelimiter = ",";


    /**
     * config user filed delimiter
     */
    private String userFieldDelimiter = ":";


    /**
     * all all ip char
     */
    private String allowAnyIpChar = "%";

    /**
     * default user config
     */
    private String allowedUsers = "root:changeit@123!:%,test:test@123!:127.0.0.1";

    /**
     * token tokenExpiration, default 6 hour.
     */
    private int tokenExpiration = 6;

}
