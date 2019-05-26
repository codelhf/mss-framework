package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/15 23:33
 */
public interface LoginService {

    ServerResponse<String> checkAccount(String account, String loginType);

    ServerResponse<User> login(String account, String md5Password, String vCode, String loginType);

    ServerResponse<String> register(String account, String md5Password, String loginType);
}
