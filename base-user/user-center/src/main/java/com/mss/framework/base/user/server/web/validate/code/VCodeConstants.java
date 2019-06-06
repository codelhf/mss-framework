package com.mss.framework.base.user.server.web.validate.code;

/**
 * @Description: 验证码常量类
 * @Auther: liuhf
 * @CreateTime: 2019/5/21 19:22
 */
public class VCodeConstants {

    public static final String VCODE_PREFIX = "vCode-";//验证码存储前缀

    public static final Integer IMAGE_CODE_COUNT = 6;//图片验证码数量

    public static final Integer IMAGE_CODE_EXPIRE_TIME = 60 * 1000;//图片验证过期时长

    public static final Integer SMS_CODE_COUNT = 6;//短信验证码数量

    public static final Integer SMS_CODE_EXPIRE_TIME = 120 * 1000;//短信验证码过期时长

    public static final Integer EMAIL_CODE_COUNT = 6;//邮箱验证码数量

    public static final Integer EMAIL_CODE_EXPIRE_TIME = 60 * 1000;//邮箱验证码过期时长
}
