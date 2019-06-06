package com.mss.framework.base.user.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 正则表达式工具类
 * @Auther: liuhf
 * @CreateTime: 2019/6/6 9:40
 */
public class RegexUtil {

    public static final String PHONE_PATTERN = "^1\\d+(0-9)$";

    public static final String EMAIL_PATTERN = "^(\\w)+(\\.-\\w+)*@[a-zA-Z0-9-]+((\\.[a-zA-Z0-9]{2,6}){1,3})$";

    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
