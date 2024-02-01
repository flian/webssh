package org.lotus.carp.webssh.config.controller.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

import static org.lotus.carp.webssh.config.controller.common.AppConst.DEFAULT_WEBSSH_SUCCESS_MSG;

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

    @JsonProperty("Msg")
    private String msg = DEFAULT_WEBSSH_SUCCESS_MSG;
    @JsonProperty("Data")
    private T data;

    public WebSshResponse(int resultCode, T data, String resultMsg) {
        this.code = resultCode;
        this.data = data;
        this.message = resultMsg;
        this.msg = resultMsg;
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
        return new WebSshResponse<E>(AppConst.OK, data, resultMsg);
    }

    public static <E> WebSshResponse<E> ok(E data) {
        return new WebSshResponse<E>(AppConst.OK, data, "操作成功");
    }

    public static <E> WebSshResponse<E> ok(int OK, E data) {
        return new WebSshResponse<E>(AppConst.OK, data);
    }

    public static <E> WebSshResponse<E> ok(String resultMsg) {
        return new WebSshResponse<E>(AppConst.OK, resultMsg);
    }


    public static <E> WebSshResponse<E> ok() {
        return new WebSshResponse<E>(AppConst.OK, "ok");
    }

    public static <T> WebSshResponse<T> ok(int OK, String resultMsg) {
        return new WebSshResponse<T>(OK, resultMsg);
    }

    public static <T> WebSshResponse<T> fail(int FAIL, String resultMsg) {
        return new WebSshResponse<T>(FAIL, resultMsg);
    }

    public static <E> WebSshResponse<E> fail() {
        return new WebSshResponse<E>(AppConst.FAIL, "fail");
    }

    public static <T> WebSshResponse<T> fail(String resultMsg) {
        return new WebSshResponse<T>(AppConst.FAIL, resultMsg);
    }
    public static <T> WebSshResponse<T> fail(T data, String resultMsg) {
        return new WebSshResponse<T>(AppConst.FAIL, data,resultMsg);
    }

    public static <E> WebSshResponse<E> fail(int FAIL, E data, String resultMsg) {
        return new WebSshResponse<E>(FAIL, data, resultMsg);
    }
}
