package com.mss.framework.base.user.server.service;

import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/7/20 17:48
 */
public interface UserService {

    User selectUserInfoByScope(String userId, String scope);

    User selectByUserId(String id);
}
