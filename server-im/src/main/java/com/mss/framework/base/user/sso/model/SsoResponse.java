package com.mss.framework.base.user.sso.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 23:18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SsoResponse {
    /**
     * 要获取的Access Token（30天的有效期）
     */
    private String access_token;

    /**
     * 用于刷新Access Token 的 Refresh Token（10年的有效期）
     */
    private String refresh_token;

    /**
     * Access Token的有效期，以秒为单位（30天的有效期）
     */
    private Long expires_in;

    /**
     * 用户信息
     */
    private User user_info;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 错误描述
     */
    private String error_description;
}
