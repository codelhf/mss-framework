package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.common.Constants;
import com.mss.framework.base.user.admin.pojo.SysUser;
import com.mss.framework.base.user.admin.service.SysRoleService;
import com.mss.framework.base.user.admin.service.SysTreeService;
import com.mss.framework.base.user.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class UserController {

    @Resource
    private UserService userService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysTreeService sysTreeService;

    @PostMapping("/login")
    public ServerResponse<SysUser> login(HttpSession session, String username, String password) {
        ServerResponse<SysUser> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }

    @GetMapping("/powers")
    public ServerResponse powers(@RequestParam("userId") String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        map.put("powers", sysTreeService.userAclTree(userId));
        return ServerResponse.createBySuccess(map);
    }

    @RequestMapping("/logout")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
}
