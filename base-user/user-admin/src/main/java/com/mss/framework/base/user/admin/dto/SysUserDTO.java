package com.mss.framework.base.user.admin.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SysUserDTO
 * @Description: SysUserDTO对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotEmpty(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度为20字以内")
    private String username;

    @NotEmpty(message = "电话不可以为空")
    @Length(min = 1, max = 13, message = "电话长度为13位")
    private String phone;

    @NotEmpty(message = "邮箱不可以为空")
    @Length(min = 1, max = 50, message = "邮箱长度为50字以内")
    private String email;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @NotNull(message = "必须指定用户状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Length(max = 200, message = "备注长度需要在200字以内")
    private String remark = "";

}