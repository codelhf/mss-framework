package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: OAuthClientUserDTO
 * @Description: OAuthClientUser实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClientUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String userId;
    
    private String clientId;
    
    private String scopeId;
    
    
}