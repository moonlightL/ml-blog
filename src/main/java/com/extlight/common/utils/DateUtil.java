package com.extlight.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date 转 String
     * @param date
     * @param pattern
     * @return
     */
    public static String formateToStr(Date date, String pattern) {
        if (pattern == null || "".equals(pattern)) {
            pattern = PATTERN;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        String result = df.format(date);
        return result;
    }

    /**
     * String 转 Date
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseToDate(String dateStr, String pattern) {
        if (pattern == null || "".equals(pattern)) {
            pattern = PATTERN;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
