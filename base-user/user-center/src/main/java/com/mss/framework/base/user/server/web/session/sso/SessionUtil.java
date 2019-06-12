//package com.mss.framework.base.user.server.web.session.sso;
//
//import com.alibaba.fastjson.JSON;
//import com.mss.framework.base.core.common.SpringContextUtil;
//import com.mss.framework.base.core.token.TokenUser;
//import com.mss.framework.base.core.util.IDUtil;
//import com.mss.framework.base.user.server.redis.RedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @Description: session工具类
// * @Auther: liuhf
// * @CreateTime: 2019/5/16 13:56
// */
//@Slf4j
//public class SessionUtil {
//
//    private static RedisUtil redisUtil;
//    // 过期时间5分钟
//    public static long EXPIRE_TIME = 5 * 60 * 1000;
//
//    public static boolean putTokenUser(HttpServletResponse response, TokenUser tokenUser, Long expiresMillis) {
//        if (redisUtil == null) {
//            redisUtil = (RedisUtil) SpringContextUtil.getBeanByName("redisUtil");
//        }
//        if (tokenUser == null) {
//            return false;
//        }
//        if (expiresMillis != null && expiresMillis > 0) {
//            EXPIRE_TIME = expiresMillis;
//        }
//        String loginToken = IDUtil.UUIDStr();
//        String jsonUser = JSON.toJSONString(tokenUser);
//        CookieUtil.writeLoginToken(response, loginToken);
//        return redisUtil.setExpire(loginToken, jsonUser, EXPIRE_TIME);
//    }
//
//    public static TokenUser getTokenUser(HttpServletRequest request) {
//        if (redisUtil == null) {
//            redisUtil = (RedisUtil) SpringContextUtil.getBeanByName("redisUtil");
//        }
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isBlank(loginToken)) {
//            return null;
//        }
//        String jsonUser = (String) redisUtil.get(loginToken);
//        if (StringUtils.isBlank(jsonUser)) {
//            return null;
//        }
//        return JSON.parseObject(jsonUser, TokenUser.class);
//    }
//
//    public static boolean deleteTokenUser(HttpServletRequest request, HttpServletResponse response) {
//        if (redisUtil == null) {
//            redisUtil = (RedisUtil) SpringContextUtil.getBeanByName("redisUtil");
//        }
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isBlank(loginToken)) {
//            return true;
//        }
//        CookieUtil.deleteLoginToken(request, response);
//        String jsonUser = (String) redisUtil.get(loginToken);
//        if (StringUtils.isBlank(jsonUser)) {
//            return true;
//        }
//        redisUtil.del(loginToken);
//        return true;
//    }
//}
