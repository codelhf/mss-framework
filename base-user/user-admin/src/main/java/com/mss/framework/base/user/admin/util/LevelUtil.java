package com.mss.framework.base.user.admin.util;

import org.apache.commons.lang3.StringUtils;

public class LevelUtil {

    public final static String ROOT = "0";

    public final static String SEPARATOR = ".";

    // 0
    // 0.1
    // 0.1.1
    // 0.1.2
    // 0.1.3
    // 0.4
    // 0.4.1
    // 0.4.2
    public static String calculateLevel(String parentLevel, int parentSeq) {
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }
        return StringUtils.join(parentLevel, SEPARATOR, parentSeq);
    }
}
