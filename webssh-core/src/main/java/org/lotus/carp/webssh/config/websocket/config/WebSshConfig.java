package org.lotus.carp.webssh.config.websocket.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
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
     * default user config string.
     */
    public static final String DEFAULT_USER_CONFIG = "root:changeit@123![RANDOM]:%,test:test@123!:127.0.0.1";

    /**
     *  config to enable random password
     */
    public boolean enableRandomPwd = true;

    /**
     * random command in password field.
     */
    public String randomPwdWord = "[RANDOM]";

    /**
     * force check allowedUsers config for production env.
     * default is true as use default setting is not safe for production env.
     * set to false close force check.
     */
    private boolean forceCheckUserConfig2Prod = true;

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
     * root:changeit@123!:%, means allow username:root,password:changeit@123! login and use webssh from any ip address
     * test:test@123!:127.0.0.1, means allow username:test,password:test@123! login, but only allow client from ip:127.0.0.1 login and use.
     */
    private String allowedUsers = DEFAULT_USER_CONFIG;

    /**
     * token tokenExpiration, default 6 hour.
     */
    private int tokenExpiration = 6;

    /**
     * webssh websocket prefix
     */
    private String webSshWebsocketPrefix = "/webssh";

    /**
     * web ssh websocket uri pattern
     */
    @Deprecated
    private String webSshWebsocketUri = "/webssh/**";

    /**
     * token name from front
     */
    private String tokenName = "token";

    /**
     * front will save password
     */
    private Boolean savePass = false;

    /**
     * debug jsch log to system error.
     */
    private Boolean debugJsch2SystemError = true;

    /**
     * default jsch connect timeout,default is 30s
     */
    private int defaultConnectTimeOut = 30 * 1000;

    /**
     * Iterator 3 time before close file process websocket connection.
     */
    private int closeWebSocketBeforeCheckCount = 3;

    /**
     * ssh term ptyType
     */
    private String webSshTermPtyType ="xterm";

    /**
     * col – terminal width
     */
    private int webSshTermCol = 80;
    /**
     * columns row – terminal height
     */
    private int webSshTermRow = 24;
    /**
     * rows wp – terminal width, pixels
     */
    private int webSshTermWp = 640;

    /**
     * hp – terminal height, pixels
     */
    private int webSshTermHp = 480;

    /**
     * term ECHO
     */
    private boolean webSshTermEcho = true;

    /**
     * Translate uppercase characters to lowercase.
     */
    private boolean webSshTermTranslate2Lowercase = false;

    /**
     * set to true to only allow private key login to remote ssh server.
     * default is false.
     */
    private boolean sshPrivateKeyOnly = false;

    /**
     * force using https for webssh.
     * config underContainer for your case.
     * current only support tomcat as underContainer.
     * other container,please config your own HttpsConfig @see TomcatHttpsConfig for reference
     */
    private boolean foreHttps = false;

    /**
     * using container,default is ''
     * while foreHttps is true and underContainer='tomcat'
     * if using tomcat,config this to tomcat will cause TomcatHttpsConfig using.
     * you may need config your own HttpsConfig for other contain.
     *
     */
    private String underContainer ="";

    /**
     * will try to auto generate https key while there is not present in classpath.
     */
    private boolean genSslKeyOnStartupIfNotPresent = false;



    /**
     * default httpPort
     */
    private int httpPort = 5132;

    /**
     * https port
     */
    private int httpsPort = 5443;
}
