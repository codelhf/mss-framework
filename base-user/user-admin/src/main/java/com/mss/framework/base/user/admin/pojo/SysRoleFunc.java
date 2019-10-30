package com.mss.framework.base.user.admin.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SysRoleFunc
 * @Description: SysRolePower实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRoleFunc implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String roleId;

    //
    private String funcId;

    //
    private String updateUser;

    //
    private String updateIp;

    //
    private Date updateTime;

}