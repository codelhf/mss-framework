package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SSOClientDetail
 * @Description: SSOClientDetail实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSOClientDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String clientName;
    
    private String description;
    
    private String redirectUrl;
    
    private String logoutUrl;
    
    private int status;
    
    
}