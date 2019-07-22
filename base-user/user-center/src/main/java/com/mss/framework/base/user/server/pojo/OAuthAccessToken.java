package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAccessToken {

    private String id;
    /**
     * Access Token
     */
    private String accessToken;
    /**
     * 关联的用户ID
     */
    private String userId;
    /**
     * 关联的用户名
     */
    private String userName;

    /**
     * 接入的appID
     */
    private String appId;

    /**
     * 过期时间戳
     */
    private Long expiresIn;

    /**
     * 授权类型，比如：authorization_code
     */
    private String grantType;
    /**
     * 可被访问的用户的权限范围，比如：basic、super
     */
    private String scope;

    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;
}