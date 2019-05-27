package com.mss.framework.base.core.token;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 用户在token中的信息
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 12:51
 */
@Getter
@Setter
public class TokenUser {

    private String id;

    private String username;

    private String phone;

    private String email;
}
