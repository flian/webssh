package org.lotus.carp.webssh.config.websocket;

import java.util.Arrays;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-02-01 10:06
 **/
public enum WebSshUrlCommandEnum {

    TERM("TERM", "/term", "终端命令行输入"),
    FILE_UPLOAD_PROGRESS("FILE_UPLOAD_PROGRESS", "/file/progress", "文件上传进度");
    private String code;
    private String url;
    private String desc;

    WebSshUrlCommandEnum(String code, String url, String desc) {
        this.code = code;
        this.url = url;
        this.desc = desc;
    }

    public static String getCmdByUri(String uri) {
        return Arrays.stream(WebSshUrlCommandEnum.values()).filter(w -> uri.contains(w.url)).map(w -> w.code).findFirst().orElse(null);
    }

    public static WebSshUrlCommandEnum getByCode(String code){
        return Arrays.stream(WebSshUrlCommandEnum.values()).filter(w -> code.equals(w.code)).findFirst().orElse(null);
    }
}
