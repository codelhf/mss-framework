package com.mss.framework.base.user.server.pojo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClientUser {

    private String id;

    private String clientId;

    private String scopeId;

    private String userId;
}