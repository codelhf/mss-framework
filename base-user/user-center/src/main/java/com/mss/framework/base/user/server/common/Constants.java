package com.mss.framework.base.user.server.common;

/**
 * @Description: 常量类
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 16:39
 */
public class Constants {

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";

    public enum StatusEnum{
        INACTIVATED(0, "INACTIVATED"),
        ACTIVATED(1, "ACTIVATED"),
        INTERDICT(2, "INTERDICT");

        private Integer code;

        private String desc;

        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public StatusEnum codeOf(int code) {
            for (StatusEnum statusEnum: StatusEnum.values()) {
                if (statusEnum.code == code) {
                    return statusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举类");
        }
    }
}
