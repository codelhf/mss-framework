package com.mss.framework.base.user.server.service;

import com.mss.framework.base.core.common.ServerResponse;

import javax.servlet.http.HttpSession;

/**
 * @Description: 验证码接口层
 * @Auther: liuhf
 * @CreateTime: 2019/5/18 22:07
 */
public interface ValidateCodeService {

    ServerResponse<String> vCodeImage(HttpSession session);

    ServerResponse<String> vCodeSms(String phone);

    ServerResponse<String> vCodeEmail(String email);
}
