package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.user.server.dao.UserMapper;
import com.mss.framework.base.user.server.enums.ScopeEnum;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/7/20 17:49
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserInfoByScope(String userId, String scope) {
        User user = userMapper.selectByPrimaryKey(userId);
        //如果是基础权限，则部分信息不返回
        if (ScopeEnum.BASIC.getCode().equalsIgnoreCase(scope)) {
            user.setPhone(null);
            user.setEmail(null);
            user.setBirthday(null);
            user.setCreateTime(null);
            user.setUpdateTime(null);
        }
        return user;
    }

    @Override
    public User selectByUserId(String id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
