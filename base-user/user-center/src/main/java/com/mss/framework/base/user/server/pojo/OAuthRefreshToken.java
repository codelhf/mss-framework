package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthRefreshToken {

    private String id;
    /**
     * 关联的表auth_access_token对应的Access Token记录
     */
    private String tokenId;
    /**
     * Refresh Token
     */
    private String refreshToken;
    /**
     * 过期时间戳
     */
    private Long expiresIn;

    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;
}