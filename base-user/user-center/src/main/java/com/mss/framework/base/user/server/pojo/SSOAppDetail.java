package com.mss.framework.base.user.server.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: SSOAppDetail
 * @Description: SSOClientDetail实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSOAppDetail {

    private String id;

    /**
     * 接入单点登录的系统名称
     */
    private String appName;

    /**
     * 接入单点登录的系统秘钥
     */
    private String appSecret;

    /**
     * 说明
     */
    private String description;

    /**
     * 请求Token的回调URL
     */
    private String redirectUrl;

    /**
     * 注销URL
     */
    private String logoutUrl;

    /**
     * 0表示未开通；1表示正常使用；2表示已被禁用
     */
    private int status;

}