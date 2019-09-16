package com.mss.framework.base.user.admin.beans;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mail {

    private String subject;

    private String message;

    private Set<String> receivers;
}
