package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAppDetail {

    private String id;

    /**
     * 接入的appID
     */
    private String appId;

    /**
     * 接入的app的名称
     */
    private String appName;

    /**
     * 接入的app的密钥
     */
    private String appSecret;

    /**
     * 回调地址
     */
    private String redirectUri;

    private String description;

    private Integer status;

    /**
     * 接入应用的开发者
     */
    private String createUser;

    private String updateUser;

    private Date createTime;

    private Date updateTime;
}