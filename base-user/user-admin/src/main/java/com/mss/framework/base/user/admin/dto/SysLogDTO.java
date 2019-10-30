package com.mss.framework.base.user.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class SysLogDTO {

    private Integer type; // LogType

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    private String fromTime;//yyyy-MM-dd HH:mm:ss

    private String toTime;//yyyy-MM-dd HH:mm:ss
}
