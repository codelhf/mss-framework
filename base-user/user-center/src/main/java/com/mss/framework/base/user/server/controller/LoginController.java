package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.LoginService;
import com.mss.framework.base.user.server.web.token.TokenUser;
import com.mss.framework.base.user.server.web.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ServerResponse login(HttpServletResponse response, String account, String md5Password, String vCode, String loginType) {
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
        String accessToken = TokenUtil.accessToken(response, tokenUser, TokenUtil.EXPIRE_TIME);
        String refreshToken = TokenUtil.refreshToken(TokenUtil.REFRESH_TOKEN_EXPIRE_TIME);

        Map<String, Object> result = new HashMap<>(4);
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("expiresIn", 1000);
        result.put("scope", "all");
        return ServerResponse.createBySuccess(result);
    }

    @GetMapping("/logout")
    public ServerResponse logout(HttpServletRequest request, HttpServletResponse response) {
        TokenUtil.deleteTokenUser(request, response);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("/register")
    public ServerResponse register(String account, String md5Password, String loginType) {
        return loginService.register(account, md5Password, loginType);
    }
}
