package com.mss.framework.base.user.admin.common;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/9/16
 */
public class Constants {

    //session中当前用户
    public static final String CURRENT_USER = "current_user";

    public enum LogStatusEnum {
        NO_RECOVER(0, "未还原过"),
        RECOVER(1, "还原过");

        private int code;
        private String desc;

        LogStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static LogStatusEnum codeOf(int code) {
            for (LogStatusEnum statusEnum : values()) {
                if (code == statusEnum.getCode()) {
                    return statusEnum;
                }
            }
            throw new RuntimeException("没有找到LogStatusEnum枚举");
        }
    }

    public enum StatusEnum {
        FREEZE(0, "冻结"),
        NORMAL(1, "正常"),
        DELETE(2, "删除");

        private int code;
        private String desc;

        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static StatusEnum codeOf(int code) {
            for (StatusEnum statusEnum : values()) {
                if (code == statusEnum.getCode()) {
                    return statusEnum;
                }
            }
            throw new RuntimeException("没有找到StatusEnum枚举");
        }
    }
}
