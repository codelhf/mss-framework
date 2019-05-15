package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户登录接口
 * @Auther: liuhf
 * @CreateTime: 2019/5/15 23:31
 */
@RestController
@RequestMapping("/user/v1.0")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ServerResponse login(String username, String md5Password) {
        ServerResponse response = loginService.login(username, md5Password);
        if (response.isSuccess()) {
            //存入session
        }
        return response;
    }

    @GetMapping("/logout")
    public ServerResponse logout() {
        User user = null;
        return ServerResponse.createBySuccess();
    }
}
