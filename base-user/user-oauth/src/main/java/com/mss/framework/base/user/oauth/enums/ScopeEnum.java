package com.mss.framework.base.user.oauth.enums;

/**
 * @Description: 权限范围
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:57
 */
public enum ScopeEnum {
    BASIC("basic","基础权限"),SUPER("super","所有权限");

    private String code;
    private String description;

    ScopeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
