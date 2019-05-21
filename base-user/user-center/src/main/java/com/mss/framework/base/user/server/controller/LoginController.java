package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.redis.RedisUtil;
import com.mss.framework.base.user.server.service.LoginService;
import com.mss.framework.base.user.server.web.token.SessionUtil;
import com.mss.framework.base.user.server.web.token.TokenUser;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public ServerResponse login(HttpServletResponse response, String username, String md5Password) {
        ServerResponse serverResponse = loginService.login(username, md5Password);
        if (serverResponse.isSuccess()) {
            //存入session
            User user = (User) serverResponse.getData();
            TokenUser tokenUser = new TokenUser();
            tokenUser.setId(user.getId());
            tokenUser.setUsername(username);
            tokenUser.setPhone(user.getPhone());
            tokenUser.setEmail(user.getEmail());
            TokenUtil.putTokenUser(response, tokenUser, null);
        }
        return serverResponse;
    }

    @GetMapping("/logout")
    public ServerResponse logout(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.deleteTokenUser(request, response);
        return ServerResponse.createBySuccess();
    }
}
