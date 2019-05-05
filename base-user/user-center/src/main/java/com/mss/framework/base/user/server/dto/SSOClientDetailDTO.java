package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SSOClientDetailDTO
 * @Description: SSOClientDetail实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSOClientDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String clientName;
    
    private String description;
    
    private String redirectUrl;
    
    private String logoutUrl;
    
    private int status;
    
    
}