package com.mss.framework.base.user.server.util;

import java.util.UUID;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 22:10
 */
public class IDUtil {

    public static synchronized String UUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
