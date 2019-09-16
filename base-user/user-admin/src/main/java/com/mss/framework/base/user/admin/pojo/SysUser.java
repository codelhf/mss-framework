package com.mss.framework.base.user.admin.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SysUser
 * @Description: SysUser实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String deptId;

    //
    private String username;

    //
    private String password;

    //
    private String phone;

    //
    private String email;

    //
    private Integer status;

    //
    private String remark;

    //
    private String operator;

    //
    private String operateIp;

    //
    private Date operateTime;

}