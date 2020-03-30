package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.SSOService;
import com.mss.framework.base.user.server.service.UserService;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    @Autowired
    private SSOService ssoService;

    @PostMapping("/logout")
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = TokenUtil.readAccessToken(request);
        List<String> cookieDomains = ssoService.getAllRedirectUri();
        boolean logout = TokenUtil.deleteSSOToken(accessToken, cookieDomains, response);
        if (logout) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @PostMapping("/update_password")
    public ServerResponse<String> updatePassword(String oldPassword, String newPassword) {
        return null;
    }

    @PostMapping("/updateUserInfo")
    public ServerResponse<String> updateUserInfo(@RequestBody User user) {
        return null;
    }
}
