package com.mss.framework.base.user.server.service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: RedisService
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 21:40
 */
public interface RedisService {

    /**
     * @description: 向Redis中存储键值对
     * @author liuhf
     * @createtime 2019/5/3 21:47
     *
     * @param [key, value]
     * @return void
     */
    void set(String key, Object value);

    /**
     * @description: 向Redis中存储键值对，并设置过期时间
     * @author liuhf
     * @createtime 2019/5/3 21:47
     *
     * @param [key, value, time, timeUnit]
     * @return void
     */
    void setWithExpire(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * @description: 从Redis中获取键值对
     * @author liuhf
     * @createtime 2019/5/3 21:47
     *
     * @param [key]
     * @return T
     */
    <T> T get(String key);

    /**
     * @description: 删除Redis中的某个KEY
     * @author liuhf
     * @createtime 2019/5/3 21:47
     *
     * @param [key]
     * @return boolean
     */
    boolean delete(String key);
}
