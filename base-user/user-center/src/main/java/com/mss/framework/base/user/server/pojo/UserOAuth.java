package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: UserOAuth
 * @Description: UserOAuth实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-26 21:51:42
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOAuth implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String userId;

    //
    private int loginType;

    //
    private String passport;

    //
    private String accessToken;

    //
    private String refreshToken;

    //
    private Date createTime;

    //
    private Date updateTime;

}