package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserDTO;
import com.mss.framework.base.user.server.service.manage.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户部分接口
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 12:19
 */
@RestController
@RequestMapping("/user/v1.0/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/update_password")
    public ServerResponse<String> updatePassword(String oldPassword, String newPassword) {
        return null;
    }

    @PostMapping("/updateUserInfo")
    public ServerResponse<String> updateUserInfo(@RequestBody UserDTO userDTO) {
        return null;
    }
}
