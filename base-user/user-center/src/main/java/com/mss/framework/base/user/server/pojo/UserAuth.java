package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.util.Date;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 18:27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuth {

    private String id;

    private String userId;

    private Integer loginType;

    private String identification;

    private String credential;

    private Date createTime;

    private Date updateTime;
}
