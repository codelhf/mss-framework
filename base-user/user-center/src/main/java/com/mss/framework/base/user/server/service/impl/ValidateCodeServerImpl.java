package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.redis.RedisUtil;
import com.mss.framework.base.user.server.service.ValidateCodeService;
import com.mss.framework.base.user.server.web.validate.code.VCodeConstants;
import com.mss.framework.base.user.server.web.validate.code.email.EmailCode;
import com.mss.framework.base.user.server.web.validate.code.image.ImageCode;
import com.mss.framework.base.user.server.web.validate.code.sms.SmsCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description: 验证码实现类
 * @Auther: liuhf
 * @CreateTime: 2019/5/18 22:08
 */
@Service
@Slf4j
public class ValidateCodeServerImpl implements ValidateCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ServerResponse<String> vCodeImage(HttpSession session) {
        ImageCode vCode = new ImageCode(session.getId(), VCodeConstants.IMAGE_CODE_COUNT);
        try {
            String code = vCode.getCode();
            boolean result = redisUtil.setExpire(vCode.getCodeKey(), code, VCodeConstants.IMAGE_CODE_EXPIRE_TIME);
            if (!result) {
                return ServerResponse.createByErrorMessage("create vCodeImage fail");
            }
            String base64Image = vCode.Base64();
            return ServerResponse.createBySuccess(base64Image);
        } catch (IOException e) {
            log.error("create vCodeImage fail:{}", e.getMessage());
            return ServerResponse.createByErrorMessage("create vCodeImage fail");
        }
    }

    @Override
    public ServerResponse<String> vCodeSms(String phone) {
        SmsCode vCode = new SmsCode(phone, VCodeConstants.SMS_CODE_COUNT);
        String code = vCode.getCode();
        if (code == null) {
            return ServerResponse.createByErrorMessage("create vCodeSms fail");
        }
        boolean result = redisUtil.setExpire(vCode.getCodeKey(), code, VCodeConstants.SMS_CODE_EXPIRE_TIME);
        if (!result) {
            return ServerResponse.createByErrorMessage("create vCodeSms fail");
        }
        return ServerResponse.createBySuccess(code);
    }

    @Override
    public ServerResponse<String> vCodeEmail(String email) {
        EmailCode vCode = new EmailCode(email, VCodeConstants.EMAIL_CODE_COUNT);
        String code = vCode.getCode();
        if (code == null) {
            return ServerResponse.createByErrorMessage("create vCodeEmail fail");
        }
        boolean result = redisUtil.setExpire(vCode.getCodeKey(), code, VCodeConstants.EMAIL_CODE_EXPIRE_TIME);
        if (!result) {
            return ServerResponse.createByErrorMessage("create vCodeEmail fail");
        }
        return ServerResponse.createBySuccess(code);
    }
}
