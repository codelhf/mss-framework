package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.SSOService;
import com.mss.framework.base.user.server.service.UserNameService;
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
 * @Description: 用户名部分
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 11:59
 */
@RestController
@RequestMapping("/user/v1.0/username")
public class UserNameController {

    @Autowired
    private UserNameService userNameService;
    @Autowired
    private SSOService ssoService;

    @GetMapping("/checkName")
    public ServerResponse<String> checkName(String username) {
        return userNameService.checkName(username);
    }

    @PostMapping("/register")
    public ServerResponse<String> register(String username, String md5Password, String vCode) {
        return userNameService.register(username, md5Password, vCode);
    }

    @PostMapping("/login_password")
    public ServerResponse loginPassword(String username, String md5Password, HttpServletResponse response) {
        ServerResponse serverResponse = userNameService.loginPassword(username, md5Password);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        User user = (User) serverResponse.getData();
        List<String> cookieDomains = ssoService.getAllRedirectUri();
        Map<String, Object> map = TokenUtil.putSSOToken(user, cookieDomains, response);
        return ServerResponse.createBySuccess(map);
    }
}
