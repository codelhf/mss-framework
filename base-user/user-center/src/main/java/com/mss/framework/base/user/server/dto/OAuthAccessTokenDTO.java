package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: OAuthAccessTokenDTO
 * @Description: OAuthAccessToken实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAccessTokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String accessToken;
    
    private String userId;
    
    private String userName;
    
    private String clientId;
    
    private long expiresIn;
    
    private String grantType;
    
    private String scope;
    
    private String createUser;
    
    private String updateUser;
    
    private Date createTime;
    
    private Date updateTime;
    
    
}