package org.lotus.carp.webssh.config.controller.common;

import lombok.Data;

import java.io.Serializable;

import static org.lotus.carp.webssh.config.controller.common.WebSshAppConst.DEFAULT_WEB_SSH_SUCCESS_MSG;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 17:15
 **/
@Data
public class WebSshResponse<T> implements Serializable {

    private int code;
    private String message;


    private String msg = DEFAULT_WEB_SSH_SUCCESS_MSG;

    private T data;


    //@JsonProperty("Duration")
    private String duration;

    public boolean isOk() {
        return WebSshAppConst.OK == code;
    }

    public WebSshResponse(int resultCode, T data, String resultMsg) {
        this.code = resultCode;
        this.data = data;
        this.message = resultMsg;
        this.msg = resultMsg;
    }

    public WebSshResponse(int resultCode, T data, String resultMsg, String timeCost) {
        this.code = resultCode;
        this.data = data;
        this.message = resultMsg;
        this.msg = resultMsg;
        this.duration = timeCost;
    }

    public WebSshResponse(int resultCode, T data) {
        this.code = resultCode;
        this.data = data;
    }

    public WebSshResponse(int resultCode, String resultMsg) {
        this.code = resultCode;
        this.message = resultMsg;
        this.msg = resultMsg;
    }


    public static <E> WebSshResponse<E> ok(int OK, E data, String resultMsg) {
        return new WebSshResponse<E>(OK, data, resultMsg);
    }

    public static <E> WebSshResponse<E> ok(E data, String resultMsg) {
        return new WebSshResponse<E>(WebSshAppConst.OK, data, resultMsg);
    }

    public static <E> WebSshResponse<E> ok(E data, String resultMsg, String timeCost) {
        return new WebSshResponse<E>(WebSshAppConst.OK, data, resultMsg, timeCost);
    }

    public static <E> WebSshResponse<E> ok(E data) {
        return new WebSshResponse<E>(WebSshAppConst.OK, data, "操作成功");
    }

    public static <E> WebSshResponse<E> ok(int OK, E data) {
        return new WebSshResponse<E>(WebSshAppConst.OK, data);
    }

    public static <E> WebSshResponse<E> ok(String resultMsg) {
        return new WebSshResponse<E>(WebSshAppConst.OK, resultMsg);
    }


    public static <E> WebSshResponse<E> ok() {
        return new WebSshResponse<E>(WebSshAppConst.OK, "ok");
    }

    public static <T> WebSshResponse<T> ok(int OK, String resultMsg) {
        return new WebSshResponse<T>(OK, resultMsg);
    }

    public static <T> WebSshResponse<T> fail(int FAIL, String resultMsg) {
        return new WebSshResponse<T>(FAIL, resultMsg);
    }

    public static <E> WebSshResponse<E> fail() {
        return new WebSshResponse<E>(WebSshAppConst.FAIL, "fail");
    }

    public static <T> WebSshResponse<T> fail(String resultMsg) {
        return new WebSshResponse<T>(WebSshAppConst.FAIL, resultMsg);
    }

    public static <T> WebSshResponse<T> fail(T data, String resultMsg) {
        return new WebSshResponse<T>(WebSshAppConst.FAIL, data, resultMsg);
    }

    public static <E> WebSshResponse<E> fail(int FAIL, E data, String resultMsg) {
        return new WebSshResponse<E>(FAIL, data, resultMsg);
    }
}
