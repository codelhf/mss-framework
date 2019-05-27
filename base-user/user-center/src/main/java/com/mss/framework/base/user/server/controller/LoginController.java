package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.token.TokenUser;
import com.mss.framework.base.core.token.TokenUtil;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/checkAccount")
    public ServerResponse checkAccount(String account, String loginType) {
        return loginService.checkAccount(account, loginType);
    }

    @PostMapping("/login")
    public ServerResponse login(String account, String md5Password, String vCode, String loginType) {
        ServerResponse serverResponse = loginService.login(account, md5Password, vCode, loginType);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        //返回登录信息
        User user = (User) serverResponse.getData();
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(user.getId());
        tokenUser.setUsername(account);
        tokenUser.setPhone(user.getPhone());
        tokenUser.setEmail(user.getEmail());
        String accessToken = TokenUtil.accessToken(tokenUser, TokenUtil.EXPIRE_TIME);
        String refreshToken = TokenUtil.refreshToken(tokenUser.getId(), TokenUtil.REFRESH_TOKEN_EXPIRE_TIME);

        Map<String, Object> result = new HashMap<>(4);
        result.put("access_token", accessToken);
        result.put("refresh_token", refreshToken);
        result.put("expires_in", 1000);
        result.put("user_info", user);
        return ServerResponse.createBySuccess(result);
    }

    @PostMapping("/register")
    public ServerResponse register(String account, String md5Password, String loginType) {
        return loginService.register(account, md5Password, loginType);
    }
}
