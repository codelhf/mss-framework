package com.mss.framework.base.user.oauth.controller;

import com.mss.framework.base.user.oauth.common.Constants;
import com.mss.framework.base.user.oauth.model.User;
import com.mss.framework.base.user.oauth.util.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Description: 用户相关controller
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 22:55
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getUserInfo")
    public JsonResult getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            return JsonResult.error("用户未登录");
        }
        return JsonResult.success(user);
    }
}
