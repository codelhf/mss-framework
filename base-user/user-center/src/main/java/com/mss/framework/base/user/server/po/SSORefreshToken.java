package com.mss.framework.base.user.server.po;

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
public class SSORefreshToken implements Serializable {
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