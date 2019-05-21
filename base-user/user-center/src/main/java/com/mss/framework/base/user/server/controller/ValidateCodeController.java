package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Description: 验证码相关接口
 * @Auther: liuhf
 * @CreateTime: 2019/5/18 22:05
 */
@RestController
@RequestMapping("/validate/code")
public class ValidateCodeController {

    @Autowired
    private ValidateCodeService validateCodeService;

    @GetMapping("/img")
    public ServerResponse<String> vCodeImage(HttpSession session) {
        return validateCodeService.vCodeImage(session);
    }

    @GetMapping("/sms/{phone}")
    public ServerResponse<String> vCodeSms(@PathVariable("phone") String phone) {
        return validateCodeService.vCodeSms(phone);
    }

    @GetMapping("/email/{email}")
    public ServerResponse<String> vCodeEmail(@PathVariable("email") String email) {
        return validateCodeService.vCodeEmail(email);
    }
}
