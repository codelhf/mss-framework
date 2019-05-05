package com.mss.framework.base.user.server.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: UserDTO
 * @Description: User实体类DTO
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String nickname;
    
    private String avatar;
    
    private int phone;
    
    private String email;
    
    private int sex;
    
    private String birthday;
    
    private Date createTime;
    
    private Date updateTime;
    
    
}