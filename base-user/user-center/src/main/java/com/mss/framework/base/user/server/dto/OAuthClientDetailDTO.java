package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: OAuthClientDetailDTO
 * @Description: OAuthClientDetail实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClientDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String clientId;
    
    private String clientName;
    
    private String clientSecret;
    
    private String redirectUri;
    
    private String description;
    
    private int status;
    
    private String createUser;
    
    private String updateUser;
    
    private Date createTime;
    
    private Date updateTime;
    
    
}