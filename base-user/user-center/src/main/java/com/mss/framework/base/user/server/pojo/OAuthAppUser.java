package com.mss.framework.base.user.server.pojo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAppUser {

    /**
     * 用户对某个接入app的授权信息表id
     */
    private String id;

    /**
     * 关联的用户ID
     */
    private String userId;

    /**
     * 关联的应用ID
     */
    private String appId;

    /**
     * 关联的用户信息范围ID
     */
    private String scopeId;
}