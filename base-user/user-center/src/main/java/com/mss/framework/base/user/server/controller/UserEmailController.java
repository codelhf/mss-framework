package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.SSOService;
import com.mss.framework.base.user.server.service.UserEmailService;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户邮箱部分
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 11:13
 */
@RestController
@RequestMapping("/user/v1.0/email")
public class UserEmailController {

    @Autowired
    private UserEmailService userEmailService;
    @Autowired
    private SSOService ssoService;

    @GetMapping("/checkEmail")
    public ServerResponse<String> checkEmail(String email) {
        return userEmailService.checkEmail(email);
    }

    @PostMapping("/register")
    public ServerResponse<String> register(String email, String md5Password, String vCode) {
        return userEmailService.register(email, md5Password, vCode);
    }

    @PostMapping("/login_password")
    public ServerResponse loginPassword(String email, String md5Password, HttpServletResponse response) {
        ServerResponse serverResponse = userEmailService.loginPassword(email, md5Password);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        User user = (User) serverResponse.getData();
        List<String> cookieDomains = ssoService.getAllRedirectUri();
        Map<String, Object> map = TokenUtil.putSSOToken(user, cookieDomains, response);
        return ServerResponse.createBySuccess(map);
    }

    @PostMapping("/login_vcode")
    public ServerResponse<User> loginVCode(String email, String vCode) {
        return userEmailService.loginVCode(email, vCode);
    }
}
