package org.lotus.carp.webssh.config.websocket.websshenum;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WebSshLoginTypeEnum {

    PRIVATE_KEY_LOGIN_TYPE("1", "login remote ssh server with private key"),
    PASSWORD_LOGIN_TYPE("0", "login remote server with password");
    private String code;

    private String desc;

    WebSshLoginTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * get login type, default is PASSWORD_LOGIN_TYPE
     * @param code
     * @return
     */
    public static WebSshLoginTypeEnum getByCode(String code) {
        return Arrays.stream(WebSshLoginTypeEnum.values()).filter(w -> code.equals(w.code)).findFirst().orElse(PASSWORD_LOGIN_TYPE);
    }
}
