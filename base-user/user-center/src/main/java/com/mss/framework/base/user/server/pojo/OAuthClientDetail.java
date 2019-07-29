package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: OAuthClientDetail
 * @Description: OAuthClientDetail实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-07-29 00:33:05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClientDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String clientId;

    //
    private String clientName;

    //
    private String clientSecret;

    //
    private String redirectUri;

    //
    private String description;

    //
    private int status;

    //
    private String createUser;

    //
    private String updateUser;

    //
    private Date createTime;

    //
    private Date updateTime;

}