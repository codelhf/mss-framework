package com.mss.framework.base.user.server.web.validate.code.sms;

import com.mss.framework.base.user.server.web.validate.code.ValidateCode;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @Description: 短信验证码
 * @Auther: liuhf
 * @CreateTime: 2019/5/18 23:11
 */
public class SmsCode extends ValidateCode {

    public SmsCode(String codeKey, Integer codeCount) {
        super(codeKey, codeCount);
    }

    @Override
    public void generate() {
        this.code = RandomStringUtils.randomNumeric(codeCount);
    }
}
