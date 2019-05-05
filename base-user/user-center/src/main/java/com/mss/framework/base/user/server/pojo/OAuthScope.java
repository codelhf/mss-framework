package com.mss.framework.base.user.server.pojo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthScope {

    private String id;
    /**
     * 可被访问的用户的权限范围，比如：basic、super
     */
    private String scopeName;

    private String description;
}