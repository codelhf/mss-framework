package com.mss.framework.base.user.server.enums;

/**
 * @Description: 权限范围
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 19:04
 */
public enum ScopeEnum {
    BASIC("basic","基础权限"),
    SUPER("super","所有权限");

    private String code;
    private String desc;

    ScopeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
