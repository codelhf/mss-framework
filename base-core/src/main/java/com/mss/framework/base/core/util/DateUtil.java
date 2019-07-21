package com.mss.framework.base.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @Description: Date相关公共方法
 * @Auther: liuhf
 * @CreateTime: 2019/5/3 16:44
 */
public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * @description: 返回当前的LocalDateTime
     * @author liuhf
     * @createtime 2019/5/27 17:24
     *
     * @param []
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime currentTime() {
        return LocalDateTime.now();
    }

    /**
     * @description: 将时间戳转换为LocalDateTime
     * @author liuhf
     * @createtime 2019/5/27 17:28
     *
     * @param [second, zoneOffset]
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime ofEpochSecond(Long second, ZoneOffset zoneOffset) {
        if (zoneOffset == null) {
            return LocalDateTime.ofEpochSecond(second, 0, ZoneOffset.ofHours(8));
        } else {
            return LocalDateTime.ofEpochSecond(second, 0, zoneOffset);
        }
    }

    /**
     * @description: 将时间字符串转化为LocalDateTime
     * @author liuhf
     * @createtime 2019/5/27 17:30
     *
     * @param [dateTimeStr]
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime toDateTime(String dateTimeStr) {
        return toDateTime(dateTimeStr, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * @description: 将时间字符串转化为LocalDateTime
     * @author liuhf
     * @createtime 2019/5/27 17:30
     *
     * @param [dateTimeStr, pattern]
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime toDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * @description: 返回几天之后的时间
     * @author liuhf
     * @createtime 2019/5/27 19:14
     *
     * @param [days]
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime nextDays(Long days) {
        return currentTime().plusDays(days);
    }

    /**
     * @description: 返回当前日期的LocalDate
     * @author liuhf
     * @createtime 2019/5/27 17:33
     *
     * @param []
     * @return java.time.LocalDate
     */
    public static LocalDate currentDate() {
        return LocalDate.now();
    }


    /**
     * @description: 将日期字符串转化为LocalDate
     * @author liuhf
     * @createtime 2019/5/27 19:13
     *
     * @param [dateStr]
     * @return java.time.LocalDate
     */
    public static LocalDate toDate(String dateStr) {
        return toDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * @description: 将日期字符串转化为LocalDate
     * @author liuhf
     * @createtime 2019/5/27 19:14
     *
     * @param [dateStr, pattern]
     * @return java.time.LocalDate
     */
    public static LocalDate toDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * @description: 返回当前时间字符串（格式化表达式：yyyy-MM-dd HH:mm:ss）
     * @author liuhf
     * @createtime 2019/5/27 17:25
     *
     * @param []
     * @return java.lang.String
     */
    public static String currentTimeStr() {
        return toDateTimeStr(currentTime(), DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * @description: 返回当前时间字符串
     * @author liuhf
     * @createtime 2019/5/27 17:33
     *
     * @param [pattern]
     * @return java.lang.String
     */
    public static String currentTimeStr(String pattern) {
        return toDateTimeStr(currentTime(), pattern);
    }

    /**
     * @description: 格式化LocalDateTime（格式化表达式：yyyy-MM-dd HH:mm:ss）
     * @author liuhf
     * @createtime 2019/5/27 17:29
     *
     * @param [dateTime]
     * @return java.lang.String
     */
    public static String toDateTimeStr(LocalDateTime dateTime) {
        return toDateTimeStr(dateTime, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * @description: 格式化LocalDateTime
     * @author liuhf
     * @createtime 2019/5/27 17:29
     *
     * @param [dateTime, pattern]
     * @return java.lang.String
     */
    public static String toDateTimeStr(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }


    /**
     * @description: 返回当前日期字符串（格式化表达式：yyyy-MM-dd）
     * @author liuhf
     * @createtime 2019/5/27 17:34
     *
     * @param []
     * @return java.lang.String
     */
    public static String currentDateStr() {
        return toDateStr(currentDate());
    }

    /**
     * @description: 格式化LocalDate
     * @author liuhf
     * @createtime 2019/5/27 17:34
     *
     * @param [date]
     * @return java.lang.String
     */
    public static String toDateStr(LocalDate date) {
        return toDateStr(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * @description: 格式化LocalDate
     * @author liuhf
     * @createtime 2019/5/27 19:13
     *
     * @param [date, pattern]
     * @return java.lang.String
     */
    public static String toDateStr(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * @description: 返回当前精确到毫秒的时间戳
     * @author liuhf
     * @createtime 2019/5/27 17:28
     *
     * @param []
     * @return java.lang.Long
     */
    public static Long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @description: 返回当前精确到秒的时间戳
     * @author liuhf
     * @createtime 2019/5/27 17:27
     *
     * @param [zoneOffset]
     * @return java.lang.Long
     */
    public static Long toEpochSecond(ZoneOffset zoneOffset) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (zoneOffset == null) {
            return dateTime.toEpochSecond(ZoneOffset.ofHours(8));
        } else {
            return dateTime.toEpochSecond(zoneOffset);
        }
    }

    /**
     * @description: 返回几天之后的时间（精确到秒的时间戳）
     * @author liuhf
     * @createtime 2019/5/27 19:14
     *
     * @param [days, zoneOffset]
     * @return java.lang.Long
     */
    public static Long nextDaysSecond(Long days, ZoneOffset zoneOffset) {
        LocalDateTime dateTime = nextDays(days);

        if (zoneOffset == null) {
            return dateTime.toEpochSecond(ZoneOffset.ofHours(8));
        } else {
            return dateTime.toEpochSecond(zoneOffset);
        }
    }

    /**
     * @description: 将天数转化为秒数
     * @author liuhf
     * @createtime 2019/5/27 19:15
     *
     * @param [days]
     * @return java.lang.Long
     */
    public static Long dayToSecond(Long days) {
        return days * 86400;
    }
}
