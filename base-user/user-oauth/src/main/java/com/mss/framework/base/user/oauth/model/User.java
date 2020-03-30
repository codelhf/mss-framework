package com.mss.framework.base.user.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/4 23:02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;

    private String nickname;

    private String phone;

    private String email;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
