package com.mss.framework.base.user.admin.dto;

import com.google.common.collect.Lists;
import com.mss.framework.base.user.admin.pojo.SysDept;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Title: SysDeptDTO
 * @Description: SysUserGroupDTO对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysDeptDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String parentId = "0";

    @NotEmpty(message = "部门名称不可以为空")
    @Length(max = 15, min = 2, message = "部门名称需要在2-15个字之间")
    private String name;

    private String level;

    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;

    @Length(max = 150, message = "备注的长度需要在150个字以内")
    private String remark;

    //
    private List<SysDeptDTO> children = Lists.newArrayList();

    public static SysDeptDTO adapter(SysDept dept) {
        SysDeptDTO deptDTO = new SysDeptDTO();
        deptDTO.setId(dept.getId());
        deptDTO.setParentId(dept.getParentId());
        deptDTO.setName(dept.getName());
        deptDTO.setLevel(dept.getLevel());
        deptDTO.setSeq(dept.getSeq());
        deptDTO.setRemark(dept.getRemark());
        return deptDTO;
    }
}