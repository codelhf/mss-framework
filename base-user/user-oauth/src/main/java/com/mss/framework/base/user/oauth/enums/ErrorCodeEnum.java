package com.mss.framework.base.user.oauth.enums;

/**
 * @Description: 错误码相关枚举类
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:56
 */
public enum  ErrorCodeEnum {
    INVALID_STATUS("invalid_status","状态码校验失败，为避免CSRF攻击，请重新登录。")
    ,UNKNOWN_ERROR("unknown_error","程序发生未知异常，请联系管理员解决。");

    /**
     * 错误码
     */
    private String error;
    /**
     * 错误描述信息
     */
    private String errorDescription;

    ErrorCodeEnum(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
