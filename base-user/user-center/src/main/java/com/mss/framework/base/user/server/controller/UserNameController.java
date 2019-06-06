package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.UserNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户名部分
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 11:59
 */
@RestController
@RequestMapping("/user/v1.0/username")
public class UserNameController {

    @Autowired
    private UserNameService userNameService;

    @GetMapping("/checkName")
    public ServerResponse<String> checkName(String username) {
        return userNameService.checkName(username);
    }

    @PostMapping("/register")
    public ServerResponse<String> register(String username, String md5Password, String vCode) {
        return userNameService.register(username, md5Password, vCode);
    }

    @PostMapping("/login_password")
    public ServerResponse<User> loginPassword(String username, String md5Password) {
        return userNameService.loginPassword(username, md5Password);
    }
}
