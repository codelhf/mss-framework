package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 12:02
 */
public interface UserNameService {

    ServerResponse<String> checkName(String username);

    ServerResponse<String> register(String username, String md5Password, String vCode);

    ServerResponse<User> loginPassword(String username, String md5Password);
}
