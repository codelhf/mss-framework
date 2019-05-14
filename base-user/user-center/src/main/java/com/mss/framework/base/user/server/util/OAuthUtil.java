package com.mss.framework.base.user.server.util;

import com.mss.framework.base.user.server.enums.ErrorCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/14 13:50
 */
public class OAuthUtil {

    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    public static Map<String, Object> successMessage(String msg) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", 200);
        result.put("msg", msg);
        return result;
    }

    public static Map<String, Object> success(String msg, Object data) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 200);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }

    public static Map<String, Object> errorMessage(String msg) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", 500);
        result.put("msg", msg);
        return result;
    }

    public static Map<String, Object> errorResponse(ErrorCodeEnum errorCodeEnum) {
        Map<String, Object> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getCode());
        result.put("error_description", errorCodeEnum.getDesc());
        return result;
    }


}
