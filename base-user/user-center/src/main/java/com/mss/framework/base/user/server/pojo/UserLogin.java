package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: UserLogin
 * @Description: UserLogin实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-15 15:24:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogin implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String userId;

    //
    private String username;

    //
    private int phone;

    //
    private String email;

    //
    private String password;

    //
    private String accessToken;

    //
    private String refreshToken;

    //
    private Date createTime;

    //
    private Date updateTime;

}