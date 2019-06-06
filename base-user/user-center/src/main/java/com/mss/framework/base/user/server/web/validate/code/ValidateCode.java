package com.mss.framework.base.user.server.web.validate.code;

/**
 * @Description: 验证码信息封装类
 * @Auther: liuhf
 * @CreateTime: 2019/5/18 22:49
 */
public abstract class ValidateCode {

    //验证码
    protected String code;
    //验证码key
    protected String codeKey;
    //验证码数量
    protected int codeCount;

    public ValidateCode(String codeKey, Integer codeCount) {
        this.codeKey = codeKey;
        this.codeCount = codeCount;
        this.generate();
    }

    public abstract void generate();

    public String getCode() {
        return code;
    }

    public String getCodeKey() {
        return VCodeConstants.VCODE_PREFIX + codeKey;
    }
}
