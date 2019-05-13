package com.mss.framework.base.user.server.service.impl;

import com.mss.framework.base.user.server.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 21:41
 */
@Service
public class RedisServiceImpl implements IRedisService {

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value) {
//        ValueOperations<String, Object> valueOperation = redisTemplate.opsForValue();
//        valueOperation.set(key, value);
    }

    @Override
    public void setWithExpire(String key, Object value, long time, TimeUnit timeUnit) {
//        BoundValueOperations<String, Object> boundValueOperations = redisTemplate.boundValueOps(key);
//        boundValueOperations.set(value);
//        boundValueOperations.expire(time,timeUnit);
    }

    @Override
    public <T> T get(String key) {
        return null;
//        ValueOperations<String, Object> valueOperation = redisTemplate.opsForValue();
//        return (T) valueOperation.get(key);
    }

    @Override
    public boolean delete(String key) {
        return false;
//        return redisTemplate.delete(key);
    }
}
