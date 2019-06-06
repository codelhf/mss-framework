package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 11:14
 */
public interface UserEmailService {

    ServerResponse<String> checkEmail(String email);

    ServerResponse<String> register(String email, String md5Password, String vCode);

    ServerResponse<User> loginPassword(String email, String md5Password);

    ServerResponse<User> loginVCode(String email, String vCode);
}
