package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: UserOAuthDTO
 * @Description: UserOAuthDTO对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-15 15:24:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOAuthDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String userId;

    //
    private int loginType;

    //
    private String identify;

    //
    private String credential;

    //
    private Date createTime;

    //
    private Date updateTime;

}