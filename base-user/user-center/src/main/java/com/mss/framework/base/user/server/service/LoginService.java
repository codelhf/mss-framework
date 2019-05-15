package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/15 23:33
 */
public interface LoginService {

    ServerResponse<User> login(String username, String md5Password);
}
