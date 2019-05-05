package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SSORefreshTokenDTO
 * @Description: SSORefreshToken实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSORefreshTokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String tokenId;
    
    private String refreshToken;
    
    private long expiresIn;
    
    private String createUser;
    
    private String updateUser;
    
    private Date createTime;
    
    private Date updateTime;
    
    
}