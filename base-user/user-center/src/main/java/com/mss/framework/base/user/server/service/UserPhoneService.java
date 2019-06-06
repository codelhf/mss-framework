package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.pojo.User;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 9:16
 */
public interface UserPhoneService {

    ServerResponse<String> checkPhone(String phone);

    ServerResponse<String> register(String phone, String md5Password, String vCode);

    ServerResponse<User> loginPassword(String phone, String md5Password);

    ServerResponse<User> loginVCode(String phone, String vCode);
}
