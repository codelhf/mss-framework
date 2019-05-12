package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserDTO;
import com.mss.framework.base.user.server.service.IRedisService;
import com.mss.framework.base.user.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 用户登录相关API
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 9:35
 */
@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    private IRedisService iRedisService;

    @Autowired
    private IUserService iUserService;

    //用户注册
    @PostMapping("/register")
    public ServerResponse<String> register(@RequestBody UserDTO userDTO) {
        return iUserService.insert(userDTO);
    }

    //登录
    @PostMapping("/login")
    public ServerResponse<UserDTO> login(String account, String password) {
        return null;
    }

    //登出
    @GetMapping("/logout")
    public ServerResponse<String> logout(String account) {
        return null;
    }

    //实际在过滤器里做验证
    //检查用户登录状态
    @PostMapping("/checkLogin")
    public ServerResponse<String> checkLogin(String account, String password) {
        return null;
    }
}
