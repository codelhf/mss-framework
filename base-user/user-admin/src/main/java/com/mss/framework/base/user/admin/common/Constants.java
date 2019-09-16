package com.mss.framework.base.user.admin.common;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/9/16
 */
public class Constants {

    //session中当前用户
    public static final String CURRENT_USER = "current_user";

    //操作日志默认状态
    public interface SysLogStatue {
        int RECOVER = 1;//还原过
        int NO_RECOVER = 0;//没有还原过
    }
}
