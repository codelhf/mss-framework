package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: UserOAuthDTO
 * @Description: UserAuth实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOAuthDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String userId;
    
    private int loginType;
    
    private String identification;
    
    private String credential;
    
    private Date createTime;
    
    private Date updateTime;
    
    
}