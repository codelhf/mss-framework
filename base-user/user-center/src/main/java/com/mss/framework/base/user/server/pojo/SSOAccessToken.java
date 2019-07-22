package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSOAccessToken {

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
     * 关联的用户来源IP
     */
    private String userIp;

    /**
     * 关联的应用ID
     */
    private String appId;

    /**
     * 表示这个Token用于什么渠道，比如：官网、APP1、APP2等等
     */
    private String channel;

    /**
     * 过期时间戳
     */
    private long expiresIn;
    
    private String createUser;
    
    private String updateUser;
    
    private Date createTime;
    
    private Date updateTime;

}