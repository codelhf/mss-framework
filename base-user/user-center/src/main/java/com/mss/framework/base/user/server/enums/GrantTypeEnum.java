package com.mss.framework.base.user.server.enums;

/**
 * @Description: 授权方式
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 19:04
 */
public enum GrantTypeEnum {
    //授权码模式
    AUTHORIZATION_CODE("authorization_code");

    private String type;

    GrantTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
