package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SSORefreshToken
 * @Description: SSORefreshToken实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSORefreshToken {

    private String id;

    /**
     * Access Token
     */
    private String tokenId;

    /**
     * Refresh Token
     */
    private String refreshToken;

    /**
     * 过期时间戳
     */
    private long expiresIn;
    
    private String createUser;
    
    private String updateUser;
    
    private Date createTime;
    
    private Date updateTime;

}