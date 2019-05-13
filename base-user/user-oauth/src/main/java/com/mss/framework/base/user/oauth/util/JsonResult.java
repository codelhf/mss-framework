package com.mss.framework.base.user.oauth.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/13 10:04
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    private JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> JsonResult success(T data) {
        return new JsonResult<T>(200, null, data);
    }

    public static <T> JsonResult success(String msg) {
        return new JsonResult<T>(200, msg, null);
    }

    public static <T> JsonResult success(String msg, T data) {
        return new JsonResult<T>(200, msg, data);
    }

    public static <T> JsonResult error(String msg) {
        return new JsonResult<T>(500, msg, null);
    }
}
