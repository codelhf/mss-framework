package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.UserLoginMapper;
import com.mss.framework.base.user.server.dao.UserMapper;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.pojo.UserLogin;
import com.mss.framework.base.user.server.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/15 23:34
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public ServerResponse<User> login(String username, String md5Password) {
        if(StringUtils.isAnyBlank(username, md5Password)) {
            return ServerResponse.createByErrorMessage("用户名或密码不能为空");
        }
        UserLogin userLogin = userLoginMapper.selectByPrimaryKey("id");
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("用户名或密码不正确");
        }
        User user = userMapper.selectByPrimaryKey(userLogin.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }
}
