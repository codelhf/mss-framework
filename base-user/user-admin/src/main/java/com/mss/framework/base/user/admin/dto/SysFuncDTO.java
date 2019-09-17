package com.mss.framework.base.user.admin.dto;

import com.mss.framework.base.user.admin.pojo.SysFunc;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: SysFuncDTO
 * @Description: SysPowerDTO对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysFuncDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotNull(message = "必须指定权限模块")
    private String parentId;

    @NotBlank(message = "权限点名称不可以为空")
    @Length(min = 2, max = 20, message = "权限点名称长度需在2-20个字以内")
    private String name;

    @Length(min = 6, max = 100, message = "权限点url长度需在6-100个字符以内")
    private String url;

    @NotNull(message = "必须指定权限点的类型")
    @Min(value = 1, message = "权限点的类型不合法")
    @Max(value = 3, message = "权限点的类型不合法")
    private Integer type;

    private String level;

    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq;

    @NotNull(message = "必须指定权限点的状态")
    @Min(value = 0, message = "权限点的状态不合法")
    @Max(value = 1, message = "权限点的状态不合法")
    private Integer status;

    @Length(max = 200, message = "备注长度在0-200字以内")
    private String remark;

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    // 子权限列表
    private List<SysFuncDTO> children = new ArrayList<>();

    public static SysFuncDTO adapter(SysFunc power) {
        SysFuncDTO powerDTO = new SysFuncDTO();
        powerDTO.setId(power.getId());
        powerDTO.setParentId(power.getParentId());
        powerDTO.setName(power.getName());
        powerDTO.setUrl(power.getUrl());
        powerDTO.setType(power.getType());
        powerDTO.setSeq(power.getSeq());
        return powerDTO;
    }
}