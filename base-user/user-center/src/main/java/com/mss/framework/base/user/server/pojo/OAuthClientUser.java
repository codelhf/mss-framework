package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: OAuthClientUser
 * @Description: OAuthClientUser实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-07-29 00:33:05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClientUser implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String userId;

    //
    private String clientId;

    //
    private String scopeId;

}