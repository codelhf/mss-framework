package com.mss.framework.base.user.server.web.token;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @Auther: liuhf
 * @CreateTime: 2019/5/16 11:49
 */
@Configuration
public class TokenUtil {

    @Value("${token.type}")
    private static String type = "jwt";

    public static String createToken() {
        return null;
    }

    public static String verifyToken(String token) {
        return token;
    }

    public static TokenUser getTokenUser(String token) {
        String jsonToken = verifyToken(token);
        return JSON.parseObject(jsonToken, TokenUser.class);
    }
}
