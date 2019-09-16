package com.mss.framework.base.user.admin.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SysPower
 * @Description: SysPower实体对象
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-09-16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class SysPower implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private String id;

    //
    private String parentId;

    //
    private String code;

    //
    private String name;

    //
    private String url;

    //
    private Integer type;

    //
    private Integer seq;

    //
    private Integer status;

    //
    private String remark;

    //
    private String updateUser;

    //
    private String updateIp;

    //
    private Date updateTime;

}