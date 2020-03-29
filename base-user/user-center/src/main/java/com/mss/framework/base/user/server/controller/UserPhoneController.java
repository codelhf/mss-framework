package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.UserPhoneService;
import com.mss.framework.base.user.server.web.token.jwt.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description: 用户手机号部分
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 9:13
 */
@RestController
@RequestMapping("/user/v1.0/phone")
public class UserPhoneController {

    @Autowired
    private UserPhoneService userPhoneService;

    @GetMapping("/checkPhone")
    public ServerResponse<String> checkPhone(String phone) {
        return userPhoneService.checkPhone(phone);
    }

    @PostMapping("/register")
    public ServerResponse<String> register(String phone, String md5Password, String vCode) {
        return userPhoneService.register(phone, md5Password, vCode);
    }

    @PostMapping("/login_password")
    public ServerResponse loginByPassword(String phone, String md5Password, HttpServletResponse response) {
        ServerResponse serverResponse = userPhoneService.loginPassword(phone, md5Password);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        User user = (User) serverResponse.getData();
        Map<String, Object> map = TokenUtil.putSSOToken(user, response);
        return ServerResponse.createBySuccess(map);
    }

    @PostMapping("/login_vcode")
    public ServerResponse<User> loginByVCode(String phone, String vCode) {
        return userPhoneService.loginVCode(phone, vCode);
    }
}
