package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClientDetail {

    private String id;

    /**
     * 接入的客户端ID
     */
    private String clientId;

    /**
     * 接入的客户端的名称
     */
    private String clientName;

    /**
     * 接入的客户端的密钥
     */
    private String clientSecret;

    /**
     * 回调地址
     */
    private String redirectUri;

    private String description;

    private Integer status;

    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;
}