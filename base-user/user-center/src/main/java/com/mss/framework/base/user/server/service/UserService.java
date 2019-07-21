package com.mss.framework.base.user.server.service;

import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/7/20 17:48
 */
public interface UserService {

    public User selectUserInfoByScope(String userId, String scope);

    public User selectByUserId(String id);
}
