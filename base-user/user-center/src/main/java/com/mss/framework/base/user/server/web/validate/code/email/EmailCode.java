package com.mss.framework.base.user.server.web.validate.code.email;

import com.mss.framework.base.user.server.web.validate.code.ValidateCode;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/21 19:17
 */
public class EmailCode extends ValidateCode {


    public EmailCode(String codeKey, Integer codeCount) {
        super(codeKey, codeCount);
    }

    @Override
    public void generate() {
        this.code = RandomStringUtils.randomAlphabetic(codeCount);
    }
}
