package com.mss.framework.base.user.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.security.MessageDigest;

public class MD5Util {

    private static Logger log = LoggerFactory.getLogger(MD5Util.class);

    @Value("${passwordSalt}")
    private static String passwordSalt;

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 返回大写MD5
     */
    private static String MD5Encode(String origin, String charsetName) {
        String resultString = new String(origin);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            log.error("MD5 加密异常{}", e);
        }
        return resultString.toUpperCase();
    }

    /**
     * MD5加密加延值
     *
     * @param origin
     * @return
     */
    public static String MD5EncodeUtf8(String origin) {
        origin = origin + passwordSalt;
        return MD5Encode(origin, "utf-8");
    }

}
