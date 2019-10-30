package com.mss.framework.base.user.admin.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SysRoleDTO
 * @Description: SysRoleDTO对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "角色名称不可以为空")
    @Length(min = 2, max = 20, message = "角色名称长度为2-20字以内")
    private String name;

    @NotNull(message = "必须指定角色的类型")
    @Min(value = 1, message = "角色的类型不合法")
    @Max(value = 2, message = "角色的类型不合法")
    private Integer type = 1;

    @NotNull(message = "必须指定角色的状态")
    @Min(value = 0, message = "角色的状态不合法")
    @Max(value = 1, message = "角色的状态不合法")
    private Integer status;

    @Length(max = 200, message = "备注长度在0-200字以内")
    private String remark;

}