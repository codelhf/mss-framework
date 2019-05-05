package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String id;

    private String nickname;

    private String avatar;

    private String phone;

    private String email;

    private Integer sex;

    private String birthday;

    private Date createTime;

    private Date updateTime;
}