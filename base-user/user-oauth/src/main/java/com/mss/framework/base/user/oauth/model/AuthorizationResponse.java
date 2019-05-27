package com.mss.framework.base.user.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: Authorization返回信息
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 23:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse {

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
     * Access Token最终的访问范围
     */
    private String scope;

    /**
     * 基于http调用Open API时所需要的Session Key，其有效期与Access Token一致
     */
    private String session_key;

    /**
     * 基于http调用Open API时计算参数签名用的签名密钥
     */
    private String session_secret;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 错误描述
     */
    private String error_description;
}
